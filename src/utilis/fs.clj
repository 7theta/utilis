;;   Copyright (c) 7theta. All rights reserved.
;;   The use and distribution terms for this software are covered by the
;;   Eclipse Public License 1.0 (http://www.eclipse.org/legal/epl-v10.html)
;;   which can be found in the LICENSE file at the root of this
;;   distribution.
;;
;;   By using this software in any fashion, you are agreeing to be bound by
;;   the terms of this license.
;;   You must not remove this notice, or any others, from this software.

(ns utilis.fs
  (:require [utilis.fn :refer [fsafe]]
            [clojure.java.io :as io]
            [clojure.string :as st])
  (:import [java.io File]
           [java.nio.file Files]
           [java.net URLEncoder]))

(declare coerce)

(defn ls
  [path & {:keys [recursive]}]
  (if recursive
    (->> (coerce path) file-seq)
    (->> (coerce path) (#(.listFiles ^File %)) seq)))

(defn mkdir
  [path & {:keys [recursive]}]
  (if recursive (.mkdirs ^File (coerce path)) (.mkdir ^File (coerce path))))

(defn rm
  [path & {:keys [recursive]}]
  (if recursive
    (->> (ls path :recursive true) reverse
         (map #(boolean (.delete ^File %)))
         (every? true?))
    (.delete ^File (coerce path))))

(defn directory?
  [path]
  (.isDirectory ^File (coerce path)))

(defn file?
  [path]
  (.isFile ^File (coerce path)))

(defn mime-type
  [path]
  (let [^File path (coerce path)]
    (try (or (->> path (.toPath) (Files/probeContentType))
             (->> path str URLEncoder/encode io/file (.toPath) (Files/probeContentType)))
         (catch java.io.EOFException e nil))))

(defn image?
  [path]
  (= "image" (first ((fsafe st/split) (mime-type (coerce path)) #"/"))))

(defn video?
  [path]
  (= "video" (first ((fsafe st/split) (mime-type (coerce path)) #"/"))))

(defmacro with-temp
  "bindings => [name ...]
  Evaluates body name(s) bound to temporary java.io.File objects
  in a location appropriate for the operating system. The temporary
  files are deleted in reverse order when exiting scope."
  [bindings & body]
  (when-not (every? symbol? bindings)
    (throw (IllegalArgumentException.
            "with-temp only allows Symbols in bindings")))
  (let [temp-dir (gensym)]
    `(let [~temp-dir (java.nio.file.Files/createTempDirectory
                      (str (gensym))
                      (make-array java.nio.file.attribute.FileAttribute 0))]
       (try
         (let ~(->> bindings
                    (map-indexed (fn [i b]
                                   [b `(.toFile (.resolve ~temp-dir (str ~i)))]))
                    (reduce concat)
                    vec)
           (try
             ~@body
             (finally ~@(map (fn [b] `(.delete ^java.io.File ~b)) (reverse bindings)))))
         (finally (java.nio.file.Files/delete ~temp-dir))))))


;;; Private

(defn- ^File coerce
  [path]
  (cond-> path (string? path) io/file))

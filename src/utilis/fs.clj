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

(defn ls-r
  [^String dir]
  (->> dir io/file file-seq))

(defn mkdir
  [^String dir]
  (let [dir (io/file dir)]
    (.mkdir dir)))

(defn mkdir-p
  [^String dir]
  (let [dir (io/file dir)]
    (when (not (.exists dir))
      (.mkdir dir))))

(defn directory?
  [^File file]
  (.isDirectory file))

(defn file?
  [^File file]
  (.isFile file))

(defn mime-type
  [^File file]
  (let [file (if (string? file) (io/file file) file)]
    (try (or (->> file (.toPath) (Files/probeContentType))
             (->> file str URLEncoder/encode io/file (.toPath) (Files/probeContentType)))
         (catch java.io.EOFException e nil))))

(defn image?
  [^File file]
  (= "image" (first ((fsafe st/split) (mime-type file) #"/"))))

(defn video?
  [^File file]
  (= "video" (first ((fsafe st/split) (mime-type file) #"/"))))

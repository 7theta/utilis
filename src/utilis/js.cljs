;;   Copyright (c) 7theta. All rights reserved.
;;   The use and distribution terms for this software are covered by the
;;   Eclipse Public License 1.0 (http://www.eclipse.org/legal/epl-v10.html)
;;   which can be found in the LICENSE file at the root of this
;;   distribution.
;;
;;   By using this software in any fashion, you are agreeing to be bound by
;;   the terms of this license.
;;   You must not remove this notice, or any others, from this software.

(ns utilis.js
  (:refer-clojure :exclude [get get-in assoc! assoc-in!])
  (:require [goog.object :as go]))

(defn get
  ([object key]
   (get object key nil))
  ([object key default]
   (when object
     (go/get object (clj->js key) default))))

(defn get-in
  ([object path]
   (get-in object path nil))
  ([object path default]
   (when object
     (or (go/getValueByKeys object (clj->js path)) default))))

(defn call
  [object key & args]
  (apply js-invoke object (clj->js key) (clj->js args)))

(defn call-in
  [object path & args]
  (if (= 1 (count path))
    (apply call object (first path) args)
    (apply call (get-in object (drop-last path)) (last path) args)))

(defn assoc!
  [object & keyvals]
  (let [object (if (nil? object) #js {} object)]
    (loop [[k v & kvs] keyvals]
      (go/set object (clojure.core/name k) v)
      (if kvs (recur kvs) object))))

(defn assoc-in!
  [object [key & rest-path :as key-path] value]
  (if (not-empty rest-path)
    (assoc! object key (assoc-in! (get object key #js {}) rest-path value))
    (assoc! object key value)))

(defn platform
  []
  (cond
    (exists? js/document) :web
    (and (exists? js/navigator)
         (= (.-product js/navigator) "ReactNative")) :react-native
    :else :nodejs))

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
  (:refer-clojure :exclude [get-in get])
  (:require [goog.object :as go]))

(defn get
  [object key]
  (go/get object (clj->js key)))

(defn get-in
  [object path]
  (when object
    (go/getValueByKeys object (clj->js path))))

(defn call
  [object key & args]
  (apply js-invoke object (clj->js key) (clj->js args)))

(defn call-in
  [object path & args]
  (if (= 1 (count path))
    (apply call object (first path) args)
    (apply call (get-in object (drop-last path)) (last path) args)))

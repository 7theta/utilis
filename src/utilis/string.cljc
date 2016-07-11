;;   Copyright (c) 7theta. All rights reserved.
;;   The use and distribution terms for this software are covered by the
;;   Eclipse Public License 1.0 (http://www.eclipse.org/legal/epl-v10.html)
;;   which can be found in the LICENSE file at the root of this
;;   distribution.
;;
;;   By using this software in any fashion, you are agreeing to be bound by
;;   the terms of this license.
;;   You must not remove this notice, or any others, from this software.

(ns utilis.string
  (:require [clojure.string :as st]))

(defn collapse-whitespace
  "Replaces a trimmed version of 's' with all consecutive runs of whitespace
  characters replaced with a single space."
  [s]
  (st/trim (st/replace s #"\s+" " ")))

(defn numeric?
  "Returns a boolean indicating whether 's' represents a numeric value"
  [s]
  (let [re #"(?:NaN|-?(?:(?:\d+|\d*(\.|/)\d+)(?:[E|e][+|-]?\d+)?|Infinity))"]
    (boolean
     #?(:clj (re-matches re s)
        :cljs (.exec (js/RegExp. (.-source re)) s)))))

(defn ensure-ends-with
  "Add 'suffix' to 's' if 's' does not already end with 'suffix'"
  [s suffix]
  (cond-> s (not (st/ends-with? s suffix)) (str suffix)))

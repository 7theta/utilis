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
  (:refer-clojure :exclude [format])
  (:require [utilis.fn :refer [fsafe]]
            [clojure.string :as st]
            #?@(:cljs [[goog.string :as gstring]
                       [goog.string.format]])))

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

(defn ensure-starts-with
  "Add 'prefix' to 's' if 's' does not already start with 'prefix'"
  [s prefix]
  (cond->> s (not (st/starts-with? s prefix)) (str prefix)))

(defn ensure-ends-with
  "Add 'suffix' to 's' if 's' does not already end with 'suffix'"
  [s suffix]
  (cond-> s (not (st/ends-with? s suffix)) (str suffix)))

(defn split
  "Splits a string 's' on the regular expression 're'. Optional
  :quote-chars can be provided to avoid splitting the string within
  quoted sections."
  [s re & {:keys [quote-chars]}]
  (if (not-empty quote-chars)
    (let [quote? (set quote-chars)
          re-find-all (fsafe
                       (fn [re s]
                         (let [m (re-matcher re s)]
                           (->> #(when (re-find m) [(.start m) (.end m)])
                                (repeatedly (count s))
                                (take-while identity)))))
          re-matches-end (fsafe
                          (fn [re s]
                            (= (count s)
                               (->> (re-find-all re s)
                                    last last))))
          quoted-sections (re-find-all (re-pattern
                                        (str "((?<![\\\\])["
                                             (apply str quote-chars)
                                             "])((?:[\\s\\S](?!(?<![\\\\])\\1))*[\\s\\S]?)\\1"))
                                       s)]
      (->> (cond-> (flatten quoted-sections)
             (not= 0 (first quoted-sections)) (conj 0)
             (not= (count s) (last quoted-sections)) (concat [(count s)]))
           (partition-all 2 1) drop-last
           (map (partial apply subs s))
           (mapcat #(if-not (quote? (first %))
                      (cond-> (st/split % re)
                        (re-matches-end re %) (conj (apply subs s (last (re-find-all re %)))))
                      [%]))
           (reduce (fn [res substr]
                     (if (and (quote? (first substr))
                              (not (re-matches-end re (last res))))
                       (-> res butlast vec
                           (conj (str (last res) substr)))
                       (conj res substr))) [])
           (remove (partial re-matches re))
           (remove empty?)
           vec))
    (st/split s re)))

(def format #?(:clj clojure.core/format
               :cljs gstring/format))

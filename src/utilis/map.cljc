;;   Copyright (c) 7theta. All rights reserved.
;;   The use and distribution terms for this software are covered by the
;;   Eclipse Public License 1.0 (http://www.eclipse.org/legal/epl-v10.html)
;;   which can be found in the LICENSE file at the root of this
;;   distribution.
;;
;;   By using this software in any fashion, you are agreeing to be bound by
;;   the terms of this license.
;;   You must not remove this notice, or any others, from this software.

(ns utilis.map
  (:require #?(:clj [utilis.coll :refer [seqable?]])
            [clojure.string :refer [blank?]]))

(defn assoc-if
  "Conditionally assoc `val` into `map` at `key` if `val` passes the test
  specified by the optional `pred`. If `pred` is not specified, the val is
  tested for truthiness."
  ([map key val]
   (assoc-if map key val identity))
  ([map key val pred]
   (cond-> map (pred val) (assoc key val))))

(defn map-keys
  "Apply the provided function 'f' to each key in map 'm'."
  [f m]
  (persistent!
   (reduce-kv (fn [a k v] (assoc! a (f k) v)) (transient {}) m)))

(defn map-vals
  "Apply the provided function 'f' to each value in map 'm'."
  [f m]
  (persistent!
   (reduce-kv (fn [a k v] (assoc! a k (f v))) (transient {}) m)))

(defn filter-vals
  "Return a map with the keys and values from 'm' for which 'pred'
  returned a truthy result for the value."
  [pred m]
  (->> m (filter (fn [[_ v]] (pred v))) (into {})))

(defn remove-vals
  "Returns a map with the keys and values from 'm' with the keys and
  values for which 'pred' returned a truthy result removed."
  [pred m]
  (filter-vals (complement pred) m))

(defn compact
  "Returns 'm' with all the keys that have nil or empty values removed.
  If the act of compaction results in all keys being removed a nil is
  returned.

  The compaction is recursive, e.g.,
    (compact {:a {:b [{} {:c {}}]}}) => nil"
  [m]
  (let [pred (fn [v] (or (nil? v)
                        #?(:clj (and (seqable? v) (empty? v))
                           :cljs (cond
                                   (or (seq? v) (coll? v)) (empty? v)
                                   (string? v) (blank? v)))))
        res (cond->> m
              (map? m) (reduce-kv (fn [m k v]
                                    (let [val (cond
                                                (map? v) (compact v)
                                                (coll? v) (keep compact v)
                                                :else v)]
                                      (cond-> m (not (pred val)) (assoc k val))))
                                  {}))]
    (when-not (pred res) res)))

(defn deep-merge-with
  "Like merge-with, but merges 'maps' recursively, applying 'f'
  only when there is a non-map at a particular level.
    (deep-merge-with + {:a {:b {:c 1 :d {:x 1 :y 2}} :e 3} :f 4}
                       {:a {:b {:c 2 :d {:z 9} :z 3} :e 100}})
    => {:a {:b {:z 3, :c 3, :d {:z 9, :x 1, :y 2}}, :e 103}, :f 4}"
  [f & maps]
  (apply (fn m [& maps]
           (if (every? map? maps)
             (apply merge-with m maps)
             (when (some identity maps)
               (apply f maps))))
         (remove nil? maps)))

(defn deep-merge
  "Like merge, but merges 'maps' recursively."
  [& maps]
  (apply deep-merge-with (fn [& vals] (->> vals (filter identity) last)) maps))

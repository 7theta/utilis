;;   Copyright (c) 7theta. All rights reserved.
;;   The use and distribution terms for this software are covered by the
;;   Eclipse Public License 1.0 (http://www.eclipse.org/legal/epl-v10.html)
;;   which can be found in the LICENSE file at the root of this
;;   distribution.
;;
;;   By using this software in any fashion, you are agreeing to be bound by
;;   the terms of this license.
;;   You must not remove this notice, or any others, from this software.

(ns utilis.coll)

(defn only
  "Returns the first element from `s` iff it's the only element in `s`.
  If `s` does not contain exactly 1 element, an IllegalAgumentException
  or js/Error is thrown. The message for the exceptions can be optionally
  specified."
  ([s]
   (only s "Sequence does not contain exactly 1 element"))
  ([s exception-message]
   (if (or (not (seq s)) (seq (rest s)))
     (throw #?(:clj (IllegalArgumentException. ^String exception-message)
               :cljs (js/Error exception-message)))
     (first s))))

(defn insert-at
  "Inserts `item` into the `coll` at `index`. Items already in `coll` after
  `index` are shifted forward to accommodate `item`.

  It is the responsibility of the caller to ensure that the index is within
  bounds."
  [coll index item]
  (if (vector? coll)
    (let [len (count coll)]
      (vec (concat (subvec coll 0 index) [item] (subvec coll index len))))
    (concat (take index coll) [item] (drop index coll))))

(defn remove-at
  "Removed the item at `index` from `coll`. Subsequent items are shifted
  to ensure that there are no gaps in the collection.

  It is the responsibility of the caller to ensure that the index is within
  bounds."
  [coll index]
  (if (vector? coll)
    (let [len (count coll)]
      (vec (concat (subvec coll 0 index) (subvec coll (min (inc index) len) len))))
    (concat (take index coll) (drop (inc index) coll))))

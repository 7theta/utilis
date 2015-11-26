;;   Copyright (c) 7theta. All rights reserved.
;;   The use and distribution terms for this software are covered by the
;;   Eclipse Public License 1.0 (http://www.eclipse.org/legal/epl-v10.html)
;;   which can be found in the file epl-v10.html at the root of this
;;   distribution.
;;
;;   By using this software in any fashion, you are agreeing to be bound by
;;   the terms of this license.
;;   You must not remove this notice, or any others, from this software.

(ns utilis.coll
  (:refer-clojure :exclude [doseq]))

#?(:clj
   (defn seqable?
     "Returns true if (seq x) will succeed, false otherwise."
     [x]
     (or (seq? x)
         (instance? clojure.lang.Seqable x)
         (nil? x)
         (instance? Iterable x)
         (.isArray (.getClass ^Object x))
         (string? x)
         (instance? java.util.Map x))))

(defn only
  "Returns the first element from 's' iff it's the only element in 's'.
  If 's' is empty or contains more than 1 element, an IllegalaAgumentException
  is thrown."
  [s]
  (if (= 1 (count s))
    (first s)
    (throw (IllegalArgumentException. "Sequence contains more than 1 element"))))

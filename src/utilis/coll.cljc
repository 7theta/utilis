;;   Copyright (c) 7theta. All rights reserved.
;;   The use and distribution terms for this software are covered by the
;;   Eclipse Public License 1.0 (http://www.eclipse.org/legal/epl-v10.html)
;;   which can be found in the LICENSE file at the root of this
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
  or js/Error is thrown. The message for the exceptions can be optionally
  specified."
  ([s]
   (only s "Sequence contains more than 1 element"))
  ([s exception-message]
   (if (= 1 (count s))
     (first s)
     (throw #?(:clj (IllegalArgumentException. ^String exception-message)
               :cljs (js/Error exception-message))))))

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
  "Returns the first element from 's' iff it's the only element in 's'.
  If 's' does not contain exactly 1 element, an IllegalAgumentException
  or js/Error is thrown. The message for the exceptions can be optionally
  specified."
  ([s]
   (only s "Sequence does not contain exactly 1 element"))
  ([s exception-message]
   (if (or (not (seq s)) (seq (rest s)))
     (throw #?(:clj (IllegalArgumentException. ^String exception-message)
               :cljs (js/Error exception-message)))
     (first s))))

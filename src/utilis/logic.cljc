;;   Copyright (c) 7theta. All rights reserved.
;;   The use and distribution terms for this software are covered by the
;;   Eclipse Public License 1.0 (http://www.eclipse.org/legal/epl-v10.html)
;;   which can be found in the LICENSE file at the root of this
;;   distribution.
;;
;;   By using this software in any fashion, you are agreeing to be bound by
;;   the terms of this license.
;;   You must not remove this notice, or any others, from this software.

(ns utilis.logic)

(defmacro xor
  "Evaluates exprs one at a time, from left to right. If a form
  returns a logical true value, xor returns that value if no other
  value also evaluates to a logical true value, otherwise it returns
  nil"
  ([] nil)
  ([x & next]
   `(let [first# ~x]
      (if first#
        (when-not (or ~@next) first#)
        (xor ~@next)))))

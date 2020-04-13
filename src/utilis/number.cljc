;;   Copyright (c) 7theta. All rights reserved.
;;   The use and distribution terms for this software are covered by the
;;   Eclipse Public License 1.0 (http://www.eclipse.org/legal/epl-v10.html)
;;   which can be found in the LICENSE file at the root of this
;;   distribution.
;;
;;   By using this software in any fashion, you are agreeing to be bound by
;;   the terms of this license.
;;   You must not remove this notice, or any others, from this software.

(ns utilis.number)

(defn clamp
  "Returns a function that takes a single value as input and clamps
  it to the range specified by `min` and `max` (inclusive)"
  [min max]
  (fn [value]
    (cond
      (< value min) min
      (> value max) max
      :else value)))

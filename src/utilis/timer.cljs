;;   Copyright (c) 7theta. All rights reserved.
;;   The use and distribution terms for this software are covered by the
;;   Eclipse Public License 1.0 (http://www.eclipse.org/legal/epl-v10.html)
;;   which can be found in the LICENSE file at the root of this
;;   distribution.
;;
;;   By using this software in any fashion, you are agreeing to be bound by
;;   the terms of this license.
;;   You must not remove this notice, or any others, from this software.

(ns utilis.timer)

(defn run-after
  "Runs `f` after `delay-ms` have elapsed"
  [f delay-ms]
  {:type :timeout
   :timer (js/setTimeout f delay-ms)})

(defn run-every
  "Runs `f` every `interval-ms` until cancelled"
  [f interval-ms]
  {:type :interval
   :timer (js/setInterval f interval-ms)})

(defn cancel
  "Cancels a previously scheduled execution of a `task`"
  [task]
  ((case (:type task)
     :timeout js/clearTimeout
     :interval js/clearInterval) (:timer task)))

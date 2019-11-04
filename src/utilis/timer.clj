;;   Copyright (c) 7theta. All rights reserved.
;;   The use and distribution terms for this software are covered by the
;;   Eclipse Public License 1.0 (http://www.eclipse.org/legal/epl-v10.html)
;;   which can be found in the LICENSE file at the root of this
;;   distribution.
;;
;;   By using this software in any fashion, you are agreeing to be bound by
;;   the terms of this license.
;;   You must not remove this notice, or any others, from this software.

(ns utilis.timer
  (:import [java.util Timer TimerTask]))

(defonce ^:private timer (delay (Timer. "utilis.timer/timer-thread")))

(defn run-after
  "Runs `f` after `delay-ms` have elapsed"
  [f delay-ms]
  (let [^TimerTask task (proxy [TimerTask] []
                          (run []
                            (f)))]
    (.schedule ^Timer @timer task ^long delay-ms)
    task))

(defn run-every
  "Runs `f` every `interval-ms` until cancelled"
  [f interval-ms]
  (let [^TimerTask task (proxy [TimerTask] []
                          (run []
                            (f)))]
    (.schedule ^Timer @timer task ^long interval-ms ^long interval-ms)
    task))

(defn cancel
  "Cancels a previously scheduled execution of a `task`"
  [^TimerTask task]
  (try (.cancel task) (catch IllegalStateException _ nil)))

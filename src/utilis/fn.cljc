;;   Copyright (c) 7theta. All rights reserved.
;;   The use and distribution terms for this software are covered by the
;;   Eclipse Public License 1.0 (http://www.eclipse.org/legal/epl-v10.html)
;;   which can be found in the LICENSE file at the root of this
;;   distribution.
;;
;;   By using this software in any fashion, you are agreeing to be bound by
;;   the terms of this license.
;;   You must not remove this notice, or any others, from this software.

(ns utilis.fn
  (:require [utilis.timer :as t]))

(defn fsafe
  "Return a function that calls 'f' iff 'f' and all the supplied arguments
  are not nil.

  An optional default value is returned if 'f' or any of it's arguments is
  nil. If a default is not provided, a nil is used as the default."
  ([f] (fsafe f nil))
  ([f default]
   (fn [& args]
     (if (or (nil? f) (some nil? args)) default (apply f args)))))

(defn apply-kw
  "Applies the fn 'f' to the argument list provided treating the last argument
  as a map containing the keyword arguments for 'f'."
  ([^clojure.lang.IFn f args]
   (apply f (apply concat args)))
  ([^clojure.lang.IFn f a args]
   (apply f a (apply concat args)))
  ([^clojure.lang.IFn f a b args]
   (apply f a b (apply concat args)))
  ([^clojure.lang.IFn f a b c args]
   (apply f a b c (apply concat args)))
  ([^clojure.lang.IFn f a b c d & args]
   (apply f a b c d (concat (butlast args) (apply concat (last args))))))

(def ^:private deferred-fns (atom {}))

(defn debounce
  "Returns a function that debounces the execution  of `f`. `f` will only run after
  `delay-ms` if no subsequent call has been made with the same arguments. If such
  a call is received, the delay will start again.

  This approach is useful for dealing with spikes where the processing of intermediate
  events is not useful until things have \"settled\""
  [f delay-ms]
  (fn [& args]
    (let [run-fn (fn []
                   (swap! deferred-fns dissoc [f args])
                   (apply f args))]
      (when-let [deferred (get @deferred-fns [f args])]
        (t/cancel deferred))
      (swap! deferred-fns assoc [f args] (t/run-after run-fn delay-ms)))))

;;   Copyright (c) 7theta. All rights reserved.
;;   The use and distribution terms for this software are covered by the
;;   Eclipse Public License 1.0 (http://www.eclipse.org/legal/epl-v10.html)
;;   which can be found in the file epl-v10.html at the root of this
;;   distribution.
;;
;;   By using this software in any fashion, you are agreeing to be bound by
;;   the terms of this license.
;;   You must not remove this notice, or any others, from this software.

(ns utilis.fn)

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

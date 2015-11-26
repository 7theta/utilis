;;   Copyright (c) 7theta. All rights reserved.
;;   The use and distribution terms for this software are covered by the
;;   Eclipse Public License 1.0 (http://www.eclipse.org/legal/epl-v10.html)
;;   which can be found in the file epl-v10.html at the root of this
;;   distribution.
;;
;;   By using this software in any fashion, you are agreeing to be bound by
;;   the terms of this license.
;;   You must not remove this notice, or any others, from this software.

(ns utilis.exception)

(defmacro throw-if
  "Throw 'v' if is throwable otherwise return it as is."
  [v]
  `(let [v# ~v] ; Ensure the 'v' is not evaluated more than once
     (cond-> v# (instance? #?(:clj Throwable
                              :cljs js/Error) v#) throw)))

(defmacro with-exception->value
  "Evaluates body & returns its result. In the event of an exception,
  'exception-mapping's can be provided which map an exception type to
  a sentinel value that can be returned in it's place.

  An exception type not specified in 'exception-map' will not be
  caught.

  e.g.,
    (with-exception->value
      [Exception :something-wrong
       NumberFormatException :could-not-covert]
      (Long/parseLong \"foo\"))"
  [exception-mapping & body]
  `(try
     ~@body
     ~@(->> exception-mapping
            (partition 2)
            (map (fn [[exception val]]
                   `(catch ~exception _# ~val))))))

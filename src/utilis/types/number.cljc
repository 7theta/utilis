;;   Copyright (c) 7theta. All rights reserved.
;;   The use and distribution terms for this software are covered by the
;;   Eclipse Public License 1.0 (http://www.eclipse.org/legal/epl-v10.html)
;;   which can be found in the file epl-v10.html at the root of this
;;   distribution.
;;
;;   By using this software in any fashion, you are agreeing to be bound by
;;   the terms of this license.
;;   You must not remove this notice, or any others, from this software.

(ns utilis.types.number
  (:require [utilis.exception :refer [with-exception->value]]))

(defn string->long
  "Attempts to convert 's' into a long. If it cannot be converted,
  an optional 'default' value is returned.

  If no default is specified, a nil is used as the default."
  ([s] (string->long s nil))
  ([s default]
   #?(:clj (with-exception->value [NumberFormatException default]
             (Long/parseLong s))
      :cljs (if (re-matches #"(^-?\d+$)" s) (js/parseInt s 10) default))))

(defn string->double
  "Attempts to convert 's' into a double. If it cannot be converted,
  an optional 'default' value is returned.

  If no default is specified, a nil is used as the default."
  ([s] (string->double s nil))
  ([s default]
   #?(:clj (with-exception->value [NumberFormatException default]
             (Double/parseDouble s))
      :cljs (if (re-matches #"(^-?\d+(?:\.\d+)?$)" s) (js/parseFloat s 10) default))))

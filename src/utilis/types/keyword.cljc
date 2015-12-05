;;   Copyright (c) 7theta. All rights reserved.
;;   The use and distribution terms for this software are covered by the
;;   Eclipse Public License 1.0 (http://www.eclipse.org/legal/epl-v10.html)
;;   which can be found in the LICENSE file at the root of this
;;   distribution.
;;
;;   By using this software in any fashion, you are agreeing to be bound by
;;   the terms of this license.
;;   You must not remove this notice, or any others, from this software.

(ns utilis.types.keyword)

(defn ->keyword
  "Attempts to convert 'v' to a keyword. If it cannot be converted,
  an options 'default' value is returned.

  If no default is specified, a nil is used as the default."
  ([v] (->keyword v nil))
  ([v default]
   (if (or (keyword? v) (string? v) (symbol? v))
     (keyword v)
     default)))

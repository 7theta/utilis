;;   Copyright (c) 7theta. All rights reserved.
;;   The use and distribution terms for this software are covered by the
;;   Eclipse Public License 1.0 (http://www.eclipse.org/legal/epl-v10.html)
;;   which can be found in the file epl-v10.html at the root of this
;;   distribution.
;;
;;   By using this software in any fashion, you are agreeing to be bound by
;;   the terms of this license.
;;   You must not remove this notice, or any others, from this software.

(ns utilis.types.boolean)

(defn ->boolean
  "Attempts to convert 'v' to a boolean. Strings equal to 'true' and 'false'
  will be coerced to their equivalent boolean value. If 'v' cannot be converted,
  an optional 'default' value is returned.

  If no default is specified, a nil is used as the default"
  ([v] (->boolean v nil))
  ([v default]
   (if (string? v)
     (get {"true" true "false" false} v default)
     (boolean v))))

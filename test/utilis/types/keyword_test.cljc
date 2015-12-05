;;   Copyright (c) 7theta. All rights reserved.
;;   The use and distribution terms for this software are covered by the
;;   Eclipse Public License 1.0 (http://www.eclipse.org/legal/epl-v10.html)
;;   which can be found in the LICENSE file at the root of this
;;   distribution.
;;
;;   By using this software in any fashion, you are agreeing to be bound by
;;   the terms of this license.
;;   You must not remove this notice, or any others, from this software.

(ns utilis.types.keyword-test
  (:require [utilis.types.keyword :refer [->keyword]]
            [clojure.test.check.generators :as gen]
            [com.gfredericks.test.chuck :refer [times]]
            [com.gfredericks.test.chuck.clojure-test #?(:clj :refer :cljs :refer-macros) [checking]]
            #?(:clj [clojure.test :refer [deftest is]]
               :cljs [cljs.test :refer-macros [deftest is] :include-macros true])))

(deftest ->keyword-should-work
  (checking "->keyword should handle strings, symbols and keywords" (times 30)
            [x gen/any]
            (if (or (keyword? x) (symbol? x) (string? x))
              (is (= (keyword x) (->keyword x)))
              (do (is (nil? (->keyword x)))
                  (is (= ::not-convertable (->keyword x ::not-convertable)))))))

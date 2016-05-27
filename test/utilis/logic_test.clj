;;   Copyright (c) 7theta. All rights reserved.
;;   The use and distribution terms for this software are covered by the
;;   Eclipse Public License 1.0 (http://www.eclipse.org/legal/epl-v10.html)
;;   which can be found in the LICENSE file at the root of this
;;   distribution.
;;
;;   By using this software in any fashion, you are agreeing to be bound by
;;   the terms of this license.
;;   You must not remove this notice, or any others, from this software.

(ns utilis.logic-test
  (:require [utilis.logic :refer [xor]]
            [clojure.test.check.generators :as gen]
            [com.gfredericks.test.chuck :refer [times]]
            [com.gfredericks.test.chuck.clojure-test :refer [checking]]
            [clojure.test :refer [deftest is]]))

(deftest xor-works
  (checking "xor should return nil when passed no arguments" 1 []
            (is (nil? (xor))))
  (checking "xor should handle multiple arguments" (times 20)
            [s (gen/vector (gen/one-of [gen/int gen/boolean (gen/return nil)
                                        gen/keyword gen/string]))]
            (is (= (eval `(xor ~@s))
                   (when (not-empty s)
                     (let [true-vals (filter identity s)]
                       (when (= 1 (count true-vals)) (first true-vals))))))))

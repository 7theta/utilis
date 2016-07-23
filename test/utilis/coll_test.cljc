;;   Copyright (c) 7theta. All rights reserved.
;;   The use and distribution terms for this software are covered by the
;;   Eclipse Public License 1.0 (http://www.eclipse.org/legal/epl-v10.html)
;;   which can be found in the LICENSE file at the root of this
;;   distribution.
;;
;;   By using this software in any fashion, you are agreeing to be bound by
;;   the terms of this license.
;;   You must not remove this notice, or any others, from this software.

(ns utilis.coll-test
  (:require [utilis.coll :refer [only]]
            [clojure.test.check.generators :as gen]
            [com.gfredericks.test.chuck :refer [times]]
            [com.gfredericks.test.chuck.clojure-test #?(:clj :refer :cljs :refer-macros) [checking]]
            #?(:clj [clojure.test :refer [deftest is]]
               :cljs [cljs.test :refer-macros [deftest is] :include-macros true])))

(deftest only-works
  (checking "only should throw on any sequence that does not contain exactly one element" (times 50)
            [s (gen/one-of [(gen/vector gen/any) (gen/list gen/any) (gen/set gen/any)
                            (gen/vector-distinct gen/any) (gen/list-distinct gen/any)])]
            (if (= 1 (count s))
              (is (= (first s) (only s)))
              (is (thrown? #?(:clj Throwable :cljs js/Error)
                           (only s))))))

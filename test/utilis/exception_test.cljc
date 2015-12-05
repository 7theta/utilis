;;   Copyright (c) 7theta. All rights reserved.
;;   The use and distribution terms for this software are covered by the
;;   Eclipse Public License 1.0 (http://www.eclipse.org/legal/epl-v10.html)
;;   which can be found in the LICENSE file at the root of this
;;   distribution.
;;
;;   By using this software in any fashion, you are agreeing to be bound by
;;   the terms of this license.
;;   You must not remove this notice, or any others, from this software.

(ns utilis.exception-test
  (:require [utilis.exception #?(:clj :refer :cljs :refer-macros) [throw-if]]
            [clojure.test.check.generators :as gen]
            [com.gfredericks.test.chuck :refer [times]]
            [com.gfredericks.test.chuck.clojure-test #?(:clj :refer :cljs :refer-macros) [checking]]
            #?(:clj [clojure.test :refer [deftest is]]
               :cljs [cljs.test :refer-macros [deftest is] :include-macros true])))

(deftest throw-if-works
  (checking "throw-if throws exceptions when detected" 1 []
            (is (thrown? #?(:clj Throwable :cljs js/Error)
                         (throw-if (#?(:clj Exception. :cljs js/Error) "foo"))))
            (is (thrown? #?(:clj Throwable :cljs js/Error)
                         (throw-if (ex-info "foo" {}))))))

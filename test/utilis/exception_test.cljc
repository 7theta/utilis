(ns utilis.exception-test
  (:require [utilis.exception #?(:clj :refer :cljs :refer-macros) [throw-if]]
            [clojure.test.check.generators :as gen]
            [com.gfredericks.test.chuck.clojure-test #?(:clj :refer :cljs :refer-macros) [checking]]
            #?(:clj [clojure.test :refer [deftest is]]
               :cljs [cljs.test :refer-macros [deftest is] :include-macros true])))

(deftest throw-if-works
  (checking "throw-if passes values unchanged" 30
            [v gen/any]
            (is (= v (throw-if v))))
  (checking "throw-if throws exceptions when detected" 1 []
            (is (thrown? #?(:clj Throwable :cljs js/Error)
                         (throw-if (#?(:clj Exception. :cljs js/Error) "foo"))))
            (is (thrown? #?(:clj Throwable :cljs js/Error)
                         (throw-if (ex-info "foo" {}))))))

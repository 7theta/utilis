(ns utilis.exception-test
  (:require [utilis.exception :refer :all]
            [clojure.test.check.generators :as gen]
            [com.gfredericks.test.chuck.clojure-test :refer [checking]]
            [com.gfredericks.test.chuck :refer [times]]
            #?(:clj [clojure.test :refer :all]
               :cljs [cljs.test :refer :all :include-macros true])))

(deftest throw-if-works
  (checking "throw-if passes values unchanged" (times 30)
            [v gen/any]
            (is (= v (throw-if v))))
  (checking "throw-if throws exceptions when detected" 1 []
            (is (thrown? Throwable (throw-if (Exception. "foo"))))
            (is (thrown? Throwable (throw-if (ex-info "foo" {}))))))

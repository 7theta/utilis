(ns utilis.types.string-test
  (:require [utilis.types.string :refer [->string]]
            [clojure.test.check.generators :as gen]
            [com.gfredericks.test.chuck.clojure-test #?(:clj :refer :cljs :refer-macros) [checking]]
            #?(:clj [clojure.test :refer [deftest is]]
               :cljs [cljs.test :refer-macros [deftest is] :include-macros true])))

(deftest ->string-should-work
  (checking "->string should handle various types" 50
            [x gen/any]
            (if (or (keyword? x) (symbol? x) (string? x))
              (is (= (name x) (->string x)))
              (is (= (str x) (->string x))))))

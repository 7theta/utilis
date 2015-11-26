(ns utilis.types.string-test
  (:require [utilis.types.string :refer :all]
            [clojure.test.check.generators :as gen]
            [com.gfredericks.test.chuck.clojure-test :refer [checking]]
            [com.gfredericks.test.chuck :refer [times]]
            #?(:clj [clojure.test :refer :all]
               :cljs [cljs.test :refer :all :include-macros true])))

(deftest ->string-should-work
  (checking "->string should handle various types" (times 50)
            [x gen/any]
            (if (or (keyword? x) (symbol? x) (string? x))
              (is (= (name x) (->string x)))
              (is (= (str x) (->string x))))))

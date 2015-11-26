(ns utilis.types.keyword-test
  (:require [utilis.types.keyword :refer [->keyword]]
            [clojure.test.check.generators :as gen]
            [com.gfredericks.test.chuck.clojure-test #?(:clj :refer :cljs :refer-macros) [checking]]
            #?(:clj [clojure.test :refer [deftest is]]
               :cljs [cljs.test :refer-macros [deftest is] :include-macros true])))

(deftest ->keyword-should-work
  (checking "->keyword should handle strings, symbols and keywords" 30
            [x gen/any]
            (if (or (keyword? x) (symbol? x) (string? x))
              (is (= (keyword x) (->keyword x)))
              (do (is (nil? (->keyword x)))
                  (is (= ::not-convertable (->keyword x ::not-convertable)))))))

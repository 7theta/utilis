(ns utilis.types.keyword-test
  (:require [utilis.types.keyword :refer :all]
            [clojure.test.check.generators :as gen]
            [com.gfredericks.test.chuck.clojure-test :refer [checking]]
            [com.gfredericks.test.chuck :refer [times]]
            #?(:clj [clojure.test :refer :all]
               :cljs [cljs.test :refer :all :include-macros true])))

(deftest ->keyword-should-work
  (checking "->keyword should handle strings, symbols and keywords" (times 30)
            [x gen/any]
            (if (or (keyword? x) (symbol? x) (string? x))
              (is (= (keyword x) (->keyword x)))
              (do (is (nil? (->keyword x)))
                  (is (= ::not-convertable (->keyword x ::not-convertable)))))))

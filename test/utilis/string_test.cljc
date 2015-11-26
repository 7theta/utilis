(ns utilis.types.string-test
  (:require  [utilis.string :refer :all]
             [clojure.string :as st]
             [clojure.test.check.generators :as gen]
             [com.gfredericks.test.chuck.clojure-test :refer [checking]]
             [com.gfredericks.test.chuck.generators :as gen']
             [com.gfredericks.test.chuck :refer [times]]
             #?(:clj [clojure.test :refer :all]
                :cljs [cljs.test :refer :all :include-macros true])))

(deftest whitespace-should-work
  (checking "whitespace? should detect all whitespace characters" (times 30)
            [ws (gen'/string-from-regex #"\s+")
             s (gen/such-that (complement whitespace?) gen/string)]
            (is (whitespace? ws))
            (is (not (whitespace? (str ws s ws))))
            (is (not (whitespace? (str s ws))))
            (is (not (whitespace? (str ws s))))))

(deftest collapse-whitespace-should-work
  (checking "collapse-whitespace should handle various types of whitespace" (times 50)
            [ws (gen'/string-from-regex #"\s+")
             x (gen/such-that (complement whitespace?) gen/string)
             y (gen/such-that (complement whitespace?) gen/string)
             z (gen/such-that (complement whitespace?) gen/string)]
            (let [s (apply str (concat x (repeat (inc (rand-int 10)) ws)
                                       y (repeat (inc (rand-int 10)) ws)
                                       z))]
              (is (not-any? whitespace? (st/split (collapse-whitespace s) #"\s"))))))

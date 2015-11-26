(ns utilis.string-test
  (:require  [utilis.string :refer [whitespace? collapse-whitespace]]
             [clojure.string :as st]
             [clojure.test.check.generators :as gen]
             #?(:clj [com.gfredericks.test.chuck.generators :as gen'])
             [com.gfredericks.test.chuck.clojure-test #?(:clj :refer :cljs :refer-macros) [checking]]
             #?(:clj [clojure.test :refer [deftest is]]
                :cljs [cljs.test :refer-macros [deftest is] :include-macros true])))

(deftest whitespace-should-work
  (checking "whitespace? should detect all whitespace characters" 30
            [ws #?(:clj (gen'/string-from-regex #"\s+")
                   :cljs (gen/elements [" " "\t" "\n"]))
             s (gen/such-that (complement whitespace?) gen/string)]
            (is (whitespace? ws))
            (is (not (whitespace? (str ws s ws))))
            (is (not (whitespace? (str s ws))))
            (is (not (whitespace? (str ws s))))))

(deftest collapse-whitespace-should-work
  (checking "collapse-whitespace should handle various types of whitespace" 50
            [ws #?(:clj (gen'/string-from-regex #"\s+")
                   :cljs (gen/elements [" " "\t" "\n"]))
             x (gen/such-that (complement whitespace?) gen/string)
             y (gen/such-that (complement whitespace?) gen/string)
             z (gen/such-that (complement whitespace?) gen/string)]
            (let [s (apply str (concat x (repeat (inc (rand-int 10)) ws)
                                       y (repeat (inc (rand-int 10)) ws)
                                       z))]
              (is (not-any? whitespace? (st/split (collapse-whitespace s) #"\s"))))))

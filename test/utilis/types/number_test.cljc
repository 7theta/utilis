(ns utilis.types.number-test
  (:require [utilis.types.number :refer [string->long string->double]]
            [utilis.types.string :refer [->string]]
            [clojure.test.check.generators :as gen]
            [com.gfredericks.test.chuck.clojure-test #?(:clj :refer :cljs :refer-macros) [checking]]
            #?(:clj [clojure.test :refer [deftest is]]
               :cljs [cljs.test :refer-macros [deftest is] :include-macros true])))

(deftest string->long-should-work
  (checking "string->long longs converted via str" 50
            [x gen/int]
            (is (= x (string->long (str x)))))
  (checking "string->long longs converted via ->string" 50
            [x gen/int]
            (is (= x (string->long (->string x)))))

  (checking "string->long should fail for non-integral values" 20
            [x gen/ratio]
            ;; Adding 3.14 to handle a generated 0
            (is (= nil (string->long (str (+ 3.14 (double x))))))
            (is (= ::bad-string (string->long (str (+ 3.14 (double x))) ::bad-string))))

  (checking "string->long should fail on random strings" 20
            [x (gen/such-that #(nil? (re-matches #"(^-?\d+$)" %))
                              gen/string)]
            (is (= nil (string->long x)))
            (is (= ::bad-string (string->long x ::bad-string)))))

(deftest string->double-should-work
  (checking "string->double longs converted via str" 50
            [x gen/ratio]
            (is (= (double x) (string->double (str (double x))))))
  (checking "string->double longs converted via ->string" 50
            [x gen/ratio]
            (is (= (double x) (string->double (->string (double x))))))

  (checking "string->double should fail on random strings" 20
            [x (gen/such-that #(nil? (re-matches #"(^-?\d+(?:\.\d+)?$)" %))
                              gen/string)]
            (is (= nil (string->double x)))
            (is (= ::bad-string (string->double x ::bad-string)))))

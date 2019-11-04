;;   Copyright (c) 7theta. All rights reserved.
;;   The use and distribution terms for this software are covered by the
;;   Eclipse Public License 1.0 (http://www.eclipse.org/legal/epl-v10.html)
;;   which can be found in the LICENSE file at the root of this
;;   distribution.
;;
;;   By using this software in any fashion, you are agreeing to be bound by
;;   the terms of this license.
;;   You must not remove this notice, or any others, from this software.

(ns utilis.map-test
  (:require [utilis.map :refer [map-keys map-vals compact
                                deep-merge-with deep-merge]]
            [clojure.test.check.generators :as gen]
            [com.gfredericks.test.chuck :refer [times]]
            [com.gfredericks.test.chuck.clojure-test #?(:clj :refer :cljs :refer-macros) [checking]]
            #?(:clj [clojure.test :refer [deftest is]]
               :cljs [cljs.test :refer-macros [deftest is] :include-macros true])))

(deftest map-keys-should-work
  (checking "map-keys should return a map" (times 30)
            [m (gen/map gen/any gen/any)]
            (is (map? (map-keys identity m))))
  (checking "map-keys should handle empty maps" 1 []
            (is (empty? (map-keys identity {}))))
  (checking "map-keys should apply f to all keys" (times 30)
            [m (gen/map gen/keyword gen/any)]
            (is (= (->> m (map-keys name) keys set)
                   (->> m keys (map name) set)))))

(deftest map-vals-should-work
  (checking "map-vals should return a map" (times 30)
            [m (gen/map gen/any gen/any)]
            (is (map? (map-vals identity m))))
  (checking "map-vals should handle empty maps" 1 []
            (is (empty? (map-vals identity {}))))
  (checking "map-vals should apply f to all values" (times 30)
            [m (gen/map gen/any gen/keyword)]
            (is (= (->> m (map-vals name) vals set)
                   (->> m vals (map name) set)))))

(deftest compact-should-work
  (checking "compact should handle empty maps" 1 []
            (is (nil? (compact {}))))
  (checking "compact should handle nil" 1 []
            (is (nil? (compact nil))))
  (checking "compact should handle maps that can't be compacted" 1 []
            (is (= {:a 1 :b 2} (compact {:a 1 :b 2}))))
  (checking "compact should return a map or nil when passed a map" (times 20)
            [m (gen/map gen/any gen/any)]
            (let [cm (compact m)]
              (is (or (nil? cm) (map? cm)))))
  (checking "compact should handle recursive maps" 1 []
            (is (= {:a "foo", :c [{:h {:k 12}}]}
                   (compact {:a "foo"
                             :b nil
                             :c [{} {:g [] :h {:k 12}}]
                             :d {:e {:f {}}}}))))
  (checking "compact should handle empty maps and collections" 1 []
            (is (nil? (compact {:a {:b [{} {:c {}}]}}))))
  (checking "compact should handle collections of strings" 1 []
            (is (= {:a ["foo" "bar"]} (compact {:a ["foo" "" "bar"]})))))

(deftest deep-merge-with-should-work
  (checking "deep-merge-with should handle nil maps" 1 []
            (is (nil? (deep-merge-with + nil nil nil)))
            (is (= {:foo 1} (deep-merge-with + nil {:foo 1})))
            (is (= {:foo 1} (deep-merge-with + {:foo 1} nil)))
            (is (= {:foo 3 :bar 1} (deep-merge-with + {:foo 1} nil {:foo 2} nil {:bar 1}))))
  (checking "deep-merge-with should handle empty maps" 1 []
            (is (= {} (deep-merge-with + {} {} {})))
            (is (= {:foo 1} (deep-merge-with + {} {:foo 1})))
            (is (= {:foo 1} (deep-merge-with + {:foo 1} {}))))
  (checking "deep-merge-with should work the same as merge-with for shallow maps" (times 20)
            [maps (gen/vector (gen/map gen/any gen/int))]
            (is (= (apply merge-with + maps)
                   (apply deep-merge-with + maps)))))

(deftest deep-merge-should-work
  (checking "deep-merge should handle nil maps" 1 []
            (is (nil? (deep-merge nil nil nil)))
            (is (= {:foo 1} (deep-merge nil {:foo 1})))
            (is (= {:foo 1} (deep-merge {:foo 1} nil)))
            (is (= {:foo 2 :bar 1} (deep-merge {:foo 1} nil {:foo 2} nil {:bar 1}))))
  (checking "deep-merge should handle empty maps" 1 []
            (is (= {} (deep-merge {} {} {})))
            (is (= {:foo 1} (deep-merge {} {:foo 1})))
            (is (= {:foo 1} (deep-merge {:foo 1} {}))))
  (checking "deep-merge should take values from last map" 1 []
            (is (= {:foo 3} (deep-merge {:foo 1} {:foo 2} {:foo 3} nil))))
  (checking "deep-merge should work the same as merge for shallow maps" (times 20)
            [maps (gen/vector (gen/map gen/any gen/int))]
            (is (= (apply merge maps)
                   (apply deep-merge maps)))))

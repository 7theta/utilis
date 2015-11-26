(ns utilis.map-test
  (:require [utilis.map :refer :all]
            [clojure.test.check.generators :as gen]
            [com.gfredericks.test.chuck.clojure-test :refer [checking]]
            [com.gfredericks.test.chuck :refer [times]]
            #?(:clj [clojure.test :refer :all]
               :cljs [cljs.test :refer :all :include-macros true])))

(deftest map-keys-should-work
  (checking "map-keys should return a map" (times 30)
            [m (gen/map gen/any gen/any)]
            (is (map? (map-keys identity m))))
  (checking "map-keys should handle empty maps" 1
            []
            (is (empty? (map-keys identity {}))))
  (checking "map-keys should apply f to all keys" (times 30)
            [m (gen/map gen/keyword gen/any)]
            (is (->> m (map-keys name) keys set)
                (->> m keys (map name) set))))

(deftest map-vals-should-work
  (checking "map-vals should return a map" (times 30)
            [m (gen/map gen/any gen/any)]
            (is (map? (map-vals identity m))))
  (checking "map-vals should handle empty maps" 1
            []
            (is (empty? (map-vals identity {}))))
  (checking "map-vals should apply f to all values" (times 30)
            [m (gen/map gen/any gen/keyword)]
            (let [res (map-vals (constantly nil) m)]
              (is (every? nil? (vals res)))
              (is (->> m (map-vals name) vals set)
                  (->> m vals (map name) set)))))

(deftest compact-should-work
  (checking "compact should handle empty maps" 1
            []
            (is (nil? (compact {}))))
  (checking "compact should handle nil" 1
            []
            (is (nil? (compact nil))))
  (checking "compact should ignore values that aren't maps" (times 30)
            [not-map (gen/such-that (complement map?) gen/any)]
            (is (if (empty? not-map)
                  (nil? (compact not-map))
                  (identical? not-map (compact not-map)))))
  (checking "compact should handle maps that can't be compacted" 1
            []
            (is (= {:a 1 :b 2} (compact {:a 1 :b 2}))))
  (checking "compact should return a map or nil when passed a map" (times 30)
            [m (gen/map gen/any gen/any)]
            (let [cm (compact m)]
              (is (or (nil? cm) (map? cm)))))
  (checking "compact should handle recursive maps" 1
            []
            (is (= {:a "foo", :c [{:h {:k 12}}]}
                   (compact {:a "foo"
                             :b nil
                             :c [{} {:g [] :h {:k 12}}]
                             :d {:e {:f {}}}}))))
  (checking "compact should handle empty maps and collections" 1
            []
            (is (= nil (compact {:a {:b [{} {:c {}}]}}))))
  (checking "compact should handle collections of strings" 1
            []
            (is (= {:a ["foo" "bar"]} (compact {:a ["foo" "" "bar"]})))))

(deftest deep-merge-with-should-work
  (checking "deep-merge-with should handle nil maps" 1
            []
            (is (nil? (deep-merge-with + nil nil nil))))
  (checking "deep-merge-with should work the same as merge-with for shallow maps" (times 30)
            [maps (gen/vector (gen/map gen/any gen/int))]
            (is (= (apply merge-with + maps)
                   (apply deep-merge-with + maps)))))

(deftest deep-merge-should-work
  (checking "deep-merge should handle nil maps" 1
            []
            (is (nil? (deep-merge nil nil nil))))
  (checking "deep-merge should work the same as merge for shallow maps" (times 30)
            [maps (gen/vector (gen/map gen/any gen/int))]
            (is (= (apply merge maps)
                   (apply deep-merge maps)))))

;;   Copyright (c) 7theta. All rights reserved.
;;   The use and distribution terms for this software are covered by the
;;   Eclipse Public License 1.0 (http://www.eclipse.org/legal/epl-v10.html)
;;   which can be found in the file epl-v10.html at the root of this
;;   distribution.
;;
;;   By using this software in any fashion, you are agreeing to be bound by
;;   the terms of this license.
;;   You must not remove this notice, or any others, from this software.

(ns utilis.fn-test
  (:require [utilis.fn :refer [fsafe apply-kw]]
            [clojure.test.check.generators :as gen]
            [com.gfredericks.test.chuck.clojure-test #?(:clj :refer :cljs :refer-macros) [checking]]
            #?(:clj [clojure.test :refer [deftest is]]
               :cljs [cljs.test :refer-macros [deftest is] :include-macros true])))

(deftest fsafe-should-work
  (checking "fsafe should not call f if it is nil" 1
            []
            (is (nil? ((fsafe nil) 1)))
            (is (= ::didnt-call ((fsafe nil ::didnt-call) 1))))

  (checking "fsafe should not call f if any argument is nil" 10
            [x (gen/vector gen/any)]
            (is (nil? (apply (fsafe vector) (conj x nil))))
            (is (= ::didnt-call (apply (fsafe vector ::didnt-call) (conj x nil))))))

(deftest apply-kw-should-work
  (checking "apply-kw should pass the map to the function as keyword arguments" 10
            [x (gen/vector gen/keyword)]
            (let [args (interleave x (range))]
              (is (= (set args)
                     (set (apply-kw vector (apply hash-map args)))))
              (is (= (set (conj args 12))
                     (set (apply-kw vector 12 (apply hash-map args))))))))

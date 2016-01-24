;;   Copyright (c) 7theta. All rights reserved.
;;   The use and distribution terms for this software are covered by the
;;   Eclipse Public License 1.0 (http://www.eclipse.org/legal/epl-v10.html)
;;   which can be found in the LICENSE file at the root of this
;;   distribution.
;;
;;   By using this software in any fashion, you are agreeing to be bound by
;;   the terms of this license.
;;   You must not remove this notice, or any others, from this software.

(ns utilis.string-test
  (:require  [utilis.string :refer [collapse-whitespace]]
             [clojure.string :as st]
             [clojure.test.check.generators :as gen]
             [com.gfredericks.test.chuck :refer [times]]
             [com.gfredericks.test.chuck.clojure-test #?(:clj :refer :cljs :refer-macros) [checking]]
             #?(:clj [clojure.test :refer [deftest is]]
                :cljs [cljs.test :refer-macros [deftest is] :include-macros true])))

(deftest collapse-whitespace-should-work
  (checking "collapse-whitespace should handle various types of whitespace" (times 50)
            [ws (gen/elements [" " "\t" "\r" "\n" "\f"])
             x (gen/such-that (complement st/blank?) gen/string)
             y (gen/such-that (complement st/blank?) gen/string)
             z (gen/such-that (complement st/blank?) gen/string)]
            (let [s (apply str (concat x (repeat (inc (rand-int 10)) ws)
                                       y (repeat (inc (rand-int 10)) ws)
                                       z))]
              (is (not-any? st/blank? (st/split (collapse-whitespace s) #"\s"))))))

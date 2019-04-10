;;   Copyright (c) 7theta. All rights reserved.
;;   The use and distribution terms for this software are covered by the
;;   Eclipse Public License 1.0 (http://www.eclipse.org/legal/epl-v10.html)
;;   which can be found in the LICENSE file at the root of this
;;   distribution.
;;
;;   By using this software in any fashion, you are agreeing to be bound by
;;   the terms of this license.
;;   You must not remove this notice, or any others, from this software.

(ns utilis.jar
  (:require [clojure.java.io :as io])
  (:import [java.util Properties]))

(defmacro version
  "Returns the version of the running jar"
  [jar-name]
  `(or
    (System/getProperty (str ~jar-name ".version"))
    (let [path# (str "META-INF/maven/" ~jar-name "/" ~jar-name "/pom.properties")
          props# (io/resource path#)]
      (when props#
        (with-open [stream# (io/input-stream props#)]
          (let [props# (doto (Properties.) (.load stream#))]
            (.getProperty props# "version")))))))

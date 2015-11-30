;;   Copyright (c) 7theta. All rights reserved.
;;   The use and distribution terms for this software are covered by the
;;   Eclipse Public License 1.0 (http://www.eclipse.org/legal/epl-v10.html)
;;   which can be found in the file epl-v10.html at the root of this
;;   distribution.
;;
;;   By using this software in any fashion, you are agreeing to be bound by
;;   the terms of this license.
;;   You must not remove this notice, or any others, from this software.

(defproject com.7theta/utilis "0.4.3"
  :description "Library of common utilities used in 7theta projects"
  :url "https://github.com/7theta/utilis"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.170"]]
  :profiles {:dev {:global-vars {*warn-on-reflection* true}
                   :plugins [[lein-cljsbuild "1.1.1"]
                             [lein-doo "0.1.6-rc.1"]]
                   :dependencies [[reloaded.repl "0.2.0"]
                                  [org.clojure/tools.namespace "0.2.11"]
                                  [org.clojure/test.check "0.8.1"]
                                  [com.gfredericks/test.chuck "0.2.3"]]
                   :source-paths ["dev"]}}
  :clean-targets ^{:protect false} ["out" "target"]
  :cljsbuild {:builds [{:id "test"
                        :source-paths ["src" "test"]
                        :compiler {:output-to "out/testable.js"
                                   :main utilis.test-runner
                                   :optimizations :advanced}}]}
  :scm {:name "git"
        :url "https://github.com/7theta/utilis"})

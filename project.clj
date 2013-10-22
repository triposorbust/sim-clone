(defproject sim-clone "0.1.0-SNAPSHOT"
  :description "Engine for simulating clonal expansion in TCR repertoire."
  :url "https://github.com/triposorbust/sim-clone/"
  :main sim-clone.core
  :aot :all
  :test-path "test"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/tools.cli "0.2.4"]])

(ns sim-clone.core
  (:gen-class :main true)
  (:require [sim-clone.information :as inf]
            [sim-clone.simulation  :as sim]
            [sim-clone.repertoire  :as rep])
  (:use [clojure.tools.cli :only [cli]]
        [clojure.java.io]))

(defn print-statistics [statistics-map]
  (do (doseq [k [:vdj-norm :cdr-norm :tot-norm]]
        (printf "%f\t" (k statistics-map)))
      (newline)))

(defn compute-statistics [repertoire]
  {:vdj-norm (inf/normalized-entropy (rep/vdj-composition repertoire))
   :cdr-norm (inf/normalized-entropy (rep/cdr-composition repertoire))
   :tot-norm (inf/normalized-entropy (rep/composition repertoire))})

(defn run-with [options]
  (let [{:keys [x-dim y-dim space number max-fraction generations]} options
        repertoire (rep/build-initial-repertoire x-dim y-dim space)
        selector (rep/make-subgroup-selector (rep/choose-clones number repertoire))
        update-function #(rep/expand-clones % selector)
        termination-function #(>= (rep/subgroup-fraction % selector))]
    (loop [r repertoire, g 1]
      (if (or (> g generations) (termination-function r))
        (print-statistics (compute-statistics r))
        (do (print-statistics (compute-statistics r))
            (recur (update-function r) (inc g)))))))

(defn parse-args [args]
  (cli args
       "Runs a simulation of clonal expansion on TCR repertoire."
       ["-h" "--help" "Prints the help banner." :default false :flag true]
       ["-x" "--x-dim" "Size VDJ space." :default 2000 :parse-fn #(int %)]
       ["-y" "--y-dim" "Size CDR space." :default 15000 :parse-fn #(int %)]
       ["-s" "--space" "Size to cover." :default 25000 :parse-fn #(int %)]
       ["-m" "--max-fraction" "Exit cond." :default 0.20 :parse-fn #(double %)]
       ["-g" "--generations" "Upper limit." :default 1000 :parse-fn #(int %)]
       ["-n" "--number" "Clones to expand." :default 1 :parse-fn #(int %)]))

(defn -main [& args]
  (let [[options args banner] (parse-args args)]
    (when (:help options)
      (println banner)
      (System/exit 0))
    (run-with options)))

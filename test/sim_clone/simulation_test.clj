(ns sim-clone.simulation-test
  (:require [clojure.test :refer :all]
            [sim-clone.simulation :refer :all]))

(deftest test-generation-limit
  (let [initial 0
        update inc
        termination (fn [_] false)
        final (run-simulation initial update termination 10)]
    (testing "Aborts after a fixed number of generations."
      (is (= 11 final)))))

(deftest test-functional-limit
  (let [initial 1
        update #(* 2 %)
        termination #(> % 256)
        final (run-simulation initial update termination 100)]
    (testing "Simulation should exit after exit condition."
      (is (= 512 final)))))

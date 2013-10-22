(ns sim-clone.information-test
  (:require [clojure.test :refer :all]
            [sim-clone.information :refer :all]))

(deftest test-log2
  (testing "Base 2 logarithm should be well defined."
    (is (= 1.0 (log2 2)))
    (is (= 5.0 (log2 32)))
    (is (let [val (log2 3)]
          (< 1 val 2)))))

(deftest test-self-information
  (testing "Self-information should be -log(p)."
    (is (= 1.0 (self-information 0.5)))
    (is (= 2.0 (self-information 0.25)))
    (is (< 1.0 (self-information 1/3) 2.0))))

(deftest test-shannon-entropy
  (testing "Expectation value of the entropy."
    (is (= 1.0 (shannon-entropy [0.5 0.5])))
    (is (= 2.0 (shannon-entropy (repeat 4 0.25))))
    (is (< (shannon-entropy [0.1 0.2 0.3 0.4]) 2.0))))

(deftest test-maximum-entropy
  (testing "Number of bits needed to distinguish n elements."
    (is (= 2.0 (max-shannon-entropy 4)))
    (is (= 5.0 (max-shannon-entropy 32)))
    (is (= 8.0 (max-shannon-entropy 256)))))


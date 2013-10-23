(ns sim-clone.repertoire-test
  (:require [clojure.test :refer :all]
            [sim-clone.repertoire :refer :all]))

(deftest test-composition
  (let [repertoire [{:fraction 0.1}
                    {:fraction 0.2}
                    {:fraction 0.3}
                    {:fraction 0.4}]]
    (testing "Returns a sequence representing repertoire fractions."
      (is (= [0.1 0.2 0.3 0.4] (composition repertoire))))))

(deftest test-expansion
  (let [repertoire [{:key :foo :fraction 0.25}
                    {:key :bar :fraction 0.25}
                    {:key :baz :fraction 0.50}]
        selector #(= (:key %) :foo)
        expanded (expand-clones repertoire selector)]
    (testing "Clonal expansion of a subpopulation."
      (is (= [0.4 0.2 0.4] (composition expanded))))))

(deftest test-repertoire-construction
  (let [repertoire (build-complete-repertoire 7 5)]
    (testing "Check that the initial repertoire is the right size."
      (is (= (count repertoire) 35)))))

(deftest test-repertoire-initialization
  (let [repertoire (build-initial-repertoire 7 5 10)]
    (testing "Check that we can limit the repertoire size."
      (is (= (count repertoire) 10)))))

(deftest test-subgroup-selector
  (let [repertoire (for [vdj (range 3) cdr (range 3)] {:vdj vdj :cdr cdr})
        selector (make-subgroup-selector [{:vdj 2 :cdr 1} {:vdj 1 :cdr 2}])
        selected (set (filter selector repertoire))]
    (testing "Ensure that the correct number of elements are chosen."
      (is (= 2 (count selected))))
    (testing "Both elements are correct/present."
      (is (contains? selected {:vdj 2 :cdr 1}))
      (is (contains? selected {:vdj 1 :cdr 2})))))

(deftest test-key-value-selector
  (let [repertoire (for [vdj (range 4) cdr (range 4) :when (<= vdj cdr)]
                     {:vdj vdj :cdr cdr :fraction nil})
        vdj-zeroes (filter (make-key-value-selector :vdj 0) repertoire)
        vdj-threes (filter (make-key-value-selector :vdj 3) repertoire)
        cdr-ones   (filter (make-key-value-selector :cdr 1) repertoire)
        cdr-twos   (filter (make-key-value-selector :cdr 2) repertoire)]
    (testing "Correct number of elements selected."
      (is (= 4 (count vdj-zeroes)))
      (is (= 1 (count vdj-threes)))
      (is (= 2 (count cdr-ones)))
      (is (= 3 (count cdr-twos))))
    (testing "Selections meet criteria."
      (is (every? #(= 0 (:vdj %)) vdj-zeroes))
      (is (every? #(= 3 (:vdj %)) vdj-threes))
      (is (every? #(= 1 (:cdr %)) cdr-ones))
      (is (every? #(= 2 (:cdr %)) cdr-twos)))))

(deftest test-vdj-composition
  (let [repertoire (for [vdj (range 3) cdr (range 3)]
                     {:vdj vdj :cdr cdr :fraction (+ (* vdj vdj) (inc cdr))})
        composition (vdj-composition repertoire)]
    (testing "Aggregates by :vdj and computes composition fractions."
      (is [6 9 18] composition))))

(deftest test-cdr-composition
  (let [repertoire (for [vdj (range 3) cdr (range 3)]
                     {:vdj vdj :cdr cdr :fraction (+ (* vdj vdj) (inc cdr))})
        composition (cdr-composition repertoire)]
    (testing "Aggregates by :cdr and computes composition fractions."
      (is [8 11 14] composition))))

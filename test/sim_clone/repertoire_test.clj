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
  (let [repertoire (build-initial-repertoire 7 5)]
    (testing "Check that the initial repertoire is the right size."
      (is (= (count repertoire) 35)))))

(ns sim-clone.repertoire)

(defn- exp [x n]
  (reduce * (repeat n x)))

(defn- power-law-building-function [n, x0, x1]
  "x = [(x1^(n+1) - x0^(n+1))*y + x0^(n+1)]^(1/(n+1))

     y: a uniform variate
     n: the distribution power
     x0,x1: range of the distribution
     x: power-law distributed variate.

   <http://mathworld.wolfram.com/RandomNumber.html>"
  (fn [uniform]
    (let [a (* uniform (- (exp x1 (inc n)) (exp x0 (inc n))))
          b (exp x0 (inc n))
          c (/ 1 (inc n))]
      (exp (+ a b) c))))

(defn- normalize [repertoire]
  (let [fraction-sum
          (reduce + (for [subpopulation repertoire] (:fraction subpopulation)))
        normalize-fraction
          (fn [fraction]
            (/ fraction fraction-sum))
        normalize-subpopulation
          (fn [subpopulation]
            (update-in subpopulation [:fraction] normalize-fraction))]
    (map normalize-subpopulation repertoire)))

(defn build-initial-repertoire [vdj-count cdr-count]
  (let [population-function (power-law-building-function 2.0 1 10000)
        initial-population (for [vdj (range vdj-count)
                                 cdr (range cdr-count)]
                             {:vdj vdj
                              :cdr cdr
                              :fraction (population-function (rand))})]
    (normalize initial-population)))

(defn select-clones [n repertoire]
  (take n (shuffle repertoire)))

(defn expand-clones [repertoire selection-function]
  (let [update-subpopulation
        (fn [subpopulation]
          (if (selection-function subpopulation)
            (assoc subpopulation :fraction (* 2 (:fraction subpopulation)))
            subpopulation))]
    (normalize (map update-subpopulation repertoire))))

(defn composition [repertoire]
  (let [normalized-repertoire (normalize repertoire)]
    (for [subpopulation normalized-repertoire] (:fraction subpopulation))))

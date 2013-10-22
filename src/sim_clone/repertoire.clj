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
  (let [fraction-sum (reduce + (map :fraction repertoire))
        normalize-fraction (fn [fraction] (/ fraction fraction-sum))
        normalize-subpopulation
          (fn [subpopulation]
            (update-in subpopulation [:fraction] normalize-fraction))]
    (map normalize-subpopulation repertoire)))

(defn- make-xy-selector [maps-to-match x-key y-key]
  (let [xys (set (for [m maps-to-match :let [x (x-key m) y (y-key m)]] [x y]))]
    (fn [m]
      (let [x (x-key m)
            y (y-key m)]
        (contains? xys [x y])))))

(defn make-subgroup-selector [subgroup]
  (make-xy-selector subgroup :vdj :cdr))

(defn make-key-value-selector [k v]
  (fn [m] (= v (k m))))

(defn subgroup-fraction [repertoire selection-function]
  (let [subgroup (filter selection-function repertoire)]
    (reduce + (map :fraction subgroup))))

(defn vdj-composition [repertoire]
  (let [vdjs (apply sorted-set (map :vdj repertoire))
        vdj-fraction (fn [vdj]
                       (subgroup-fraction repertoire
                                          (make-key-value-selector :vdj vdj)))]
    (map vdj-fraction vdjs)))

(defn cdr-composition [repertoire]
  (let [cdrs (apply sorted-set (map :cdr repertoire))
        cdr-fraction (fn [cdr]
                       (subgroup-fraction repertoire
                                          (make-key-value-selector :cdr cdr)))]
    (map cdr-fraction cdrs)))

(defn build-complete-repertoire [vdj-count cdr-count]
  (let [population-function (power-law-building-function 2.0 0.0 1.0)
        initial-population (for [vdj (range vdj-count)
                                 cdr (range cdr-count)]
                             {:vdj vdj
                              :cdr cdr
                              :fraction (population-function (rand))})]
    (normalize initial-population)))

(defn build-initial-repertoire [vdj-count cdr-count number-of-combinations]
  (->> (build-complete-repertoire vdj-count cdr-count)
       (sort-by :fraction)
       (reverse)
       (take number-of-combinations)
       (normalize)))

(defn choose-clones [n repertoire]
  (take n (shuffle repertoire)))

(defn expand-clones [repertoire selection-function]
  (let [update-subpopulation
        (fn [subpopulation]
          (if (selection-function subpopulation)
            (assoc subpopulation :fraction (* 2 (:fraction subpopulation)))
            subpopulation))]
    (normalize (map update-subpopulation repertoire))))

(defn composition [repertoire]
  (map :fraction (normalize repertoire)))

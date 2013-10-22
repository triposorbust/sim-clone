(ns sim-clone.information)

(defn- log2 [n]
  (/ (Math/log n) (Math/log 2)))

(defn self-information [p]
  (* -1 (log2 p)))

(defn shannon-entropy [ps]
  (let [sis (map self-information ps)]
    (reduce + (map #(apply * %&) ps sis))))

(defn max-shannon-entropy [n]
  (log2 n))

(defn normalized-entropy [ps]
  (let [entropy (shannon-entropy ps)
        maximum (max-shannon-entropy (count ps))]
    (/ entropy maximum)))

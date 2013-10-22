(ns sim-clone.simulation)

(defn run-simulation
  [initial-world-state update-function termination-function generation-limit]
  (loop [world-state initial-world-state
         generation 1]
    (if (or (> generation generation-limit)
            (termination-function world-state))
      world-state
      (recur (update-function world-state) (inc generation)))))

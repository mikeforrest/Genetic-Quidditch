;; Neighbor maintenance for pucks.

(ns pucks.gp
  (:use pucks.globals pucks.util pucks.vec2D))

(defn rand-relative-position []
  (rotation->relative-position (- (rand two-pi) pi)))

(defn rand-urge []
  (- (rand 2.0) 1.0))

(defn rand-probability []
  (rand 1.0))

(defn mutate-urge [u]
  (max -1.0 (min 1.0 (+ u (- (rand 0.1) 0.05)))))

(defn mutate-probability [p]
  (max 0.0 (min 1.0 (+ p (- (rand 0.05) 0.025)))))

(defn chasemutate
  "Returns a mutated version of the provided genome."
  [genome]
  (-> genome
    (assoc :random-urge (mutate-urge (:random-urge genome)))
    (assoc :beater-urge (mutate-urge (:beater-urge genome)))
    (assoc :beater-urge (mutate-urge (:obs-urge genome)))
    (assoc :spawn-probability (mutate-probability (:spawn-probability genome)))))
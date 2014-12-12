;; Definitions for vent agents.

(ns pucks.agents.vent2
  (:use quil.core 
        [pucks globals util vec2D]
        pucks.agents.generic))

(defn draw-vent2 [p]
  (let [[x y] (:position p)
        radius (:radius p)
        [r g b] (:color p)
        [core-r core-g core-b] [238 118 0]
        core-diameter (int (* (float radius) (:energy p)))]
    (push-matrix)
    (translate x y)
    (rotate (:rotation p))
    ;; membrane
    (fill r g b 100)
    (ellipse 0 0 (* radius 2) (* radius 2))
    ;; core
    (fill 255 255 255)
    (ellipse 0 0 radius radius)
    (fill core-r core-g core-b 196) 
    (ellipse 0 0 core-diameter core-diameter)
    (pop-matrix)))

;; Give energy to any overlapping mobile agent, asking nothing in return.

(defn vent2-proposals [p]
  {:transfer (into [] (for [recipient (filter :team1 (:overlaps p))]
                        {:self (:id p)
                         :other (:id recipient)
                         :bid {:energy 0.01}
                         :ask {}}))})

(defn vent2 []
  (merge (generic)
         {:vent2 true
          :vent true
          :radius 50
          :draw-function draw-vent2
          :proposal-function vent2-proposals}))

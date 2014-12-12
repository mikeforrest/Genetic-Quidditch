;; Definition for user agents. This is a good template to build on to produce
;; smarter agents.

(ns pucks.agents.snitch  
  (:use quil.core
        [pucks globals util vec2D]
        [pucks.agents active]))

(defn snitch-proposals [p item]
  (let [wall (filter :stone (:sensed p))
        zap (filter :zapper (:sensed p))]
  {:acceleration 1
   :rotation 
   (if (empty? zap)
     (if (> (mod (:steps p) 400) 325)
       (if (empty? wall)
         (:rotation p)
         (+ (direction->rotation (:position (first wall))) pi))
       (direction->rotation
       [(- (rand-int 800) (rand-int 800)) (- (rand-int 800) (rand-int 800))]))
     (+ (direction->rotation (:position (first zap))) pi))       
   
   :transfer (into [] (for [recipient (filter :seeker (:overlaps p))]
                        {:self (:id p)
                        :other (:id recipient)
                        :bid {:inventory item}
                        :ask {}}))}))

(defn snitch [item]
  (merge (active)
         {:snitch true
          :inventory [item]
          :proposal-function #(snitch-proposals % item)
          :color [255 255 0]}))
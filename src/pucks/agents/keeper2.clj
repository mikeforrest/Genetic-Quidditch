;; Definition for user agents. This is a good template to build on to produce
;; smarter agents.

(ns pucks.agents.keeper2 
  (:use [pucks globals util vec2D]
        [pucks.agents active]))

(defn keeper2-proposals [p]
  (let [target (filter :team1 (:sensed p))
        avoid (filter :beater (:sensed p))
        wall (filter :stone (:sensed p))
        zap (filter :zapper (:sensed p))]
{:acceleration  (if (> (mod (:steps p) 100) 75)
                     0.8
                     0.5)  
   :rotation
        (if (empty? zap)
          (if (empty? target)
              (if (empty? avoid) 
                 (if (empty? wall)
                    (/ pi 2)
                   (+ (direction->rotation (:position (last wall))) pi))
                 (+ (direction->rotation (:position (first avoid))) pi))
               (direction->rotation (:position (first target))))
          (+ (direction->rotation (:position (first zap))) pi))}))
(defn keeper2 []
  (merge (active)
         {:keeper true
          :team2 true
          :player true
          :proposal-function keeper2-proposals
          :color [0 0 255]}))

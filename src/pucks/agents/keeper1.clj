;; Definition for user agents. This is a good template to build on to produce
;; smarter agents.

(ns pucks.agents.keeper1 
  (:use [pucks globals util vec2D]
        [pucks.agents active]))

(defn keeper1-proposals [p]
  (let [target (filter :team2 (:sensed p))
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
                   (/(* 3 pi) 2)
                   (+ (direction->rotation (:position (last wall))) pi))
                 (+ (direction->rotation (:position (first avoid))) pi))
               (direction->rotation (:position (first target))))
          (+ (direction->rotation (:position (first zap))) pi))}))
 


;(if (empty? guide)
      ; (/ (* pi 3) 2)
     ;  (direction->rotation [(rand-int 10) (rand-int 10)]))}))               
               ;(if (> (first (:position self)) 300)
                ;      (+ (direction->rotation (:position self)) (/ pi 2)) 
                  ;    (direction->rotation [(rand-int 10) (rand-int 10)]))
                ; (+ (direction->rotation (:position (first guide))) (/ pi 2)))}))

(defn keeper1 []
  (merge (active)
         {:keeper true
          :team1 true
          :player true
          :proposal-function keeper1-proposals
          :color [0 0 255]}))

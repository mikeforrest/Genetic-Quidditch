;; Definition for user agents. This is a good template to build on to produce
;; smarter agents.

(ns pucks.agents.seeker1  
  (:use [pucks globals util vec2D]
        [pucks.agents active]))

(defn seeker1-proposals [p]
   
       (let [target (filter :snitch(:sensed p))
            avoid (filter :beater (:sensed p))
            info (filter :seeker (:sensed p))
            wall (filter :stone (:sensed p))
              zap (filter :zapper (:sensed p))]
      {:acceleration (if (empty? avoid)   
                       (if (> (mod (:steps p) 100) 25)
                         1
                         0.7)  
                       (* 0.25 (- (length (:velocity p)))))
       :rotation
            (if (empty? zap)
            (if (empty? target)
              (if (empty? wall)
                (if (empty? avoid) 
                   (if (empty? info)
                       (direction->rotation [400 400])
                        (+ (direction->rotation (:position (first info))) pi))
                (+ (direction->rotation (:position (first avoid))) pi))
                (+ (direction->rotation (:position (last wall))) pi))
           (direction->rotation (:position (first target))))
             (+ (direction->rotation (:position (first zap))) pi))
     :fire-torpedo 
          (if (empty? (:inventory p))
             false
             (zero? (rand-int 20)))}))

(defn seeker1 []
  (merge (active)
         {:seeker true
          :team1 true
          :player true
          :proposal-function seeker1-proposals
          :color [255 255 255]}))

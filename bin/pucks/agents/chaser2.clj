;; Definition for user agents. This is a good template to build on to produce
;; smarter agents.

(ns pucks.agents.chaser2
  (:use [pucks globals util vec2D gp]
        [pucks.agents active]))



(defn chaser2-proposals [p]
 (if-let [g (:genome (:memory p))]
   (let [target (filter :vent1 (:sensed p))
        wall (filter :stone (:sense p))
        zap (filter :zapper (:sensed p))
        obs (filter :obs (:sensed p))
        beat (filter :beater2 (:sensed p))]
     (merge {:acceleration (if (empty? beat)   
                   (if (> (mod (:steps p) 100) 25)
                     1
                     0.1)       
                   (* 0.25 (- (length (:velocity p)))))
            :rotation 
              (if (empty? zap)
                (if (and (empty? target) (not (empty? wall)))
                   (direction->rotation [50 200])
                  (if (empty? target)
                     (if (empty? obs)
                       (- (direction->rotation [100 175]) (/ (* 3 pi) 2))
                       (relative-position->rotation 
                        (+v (rotation->relative-position (:rotation p))      
                              (if (empty? obs)
                                [0 0]
                                (*v (:obs-urge g)
                                    (limit1 (apply avgv (map :position obs)))))
                              (if (empty? zap)
                                [0 0]
                                (*v (:beater-urge g)
                                    (limit1 (apply avgv (map :position beat)))))                          
                              (*v (:random-urge g)
                                  (rand-relative-position)))))
                     (if (< (:energy (first target)) 0.01)
                        (+ (direction->rotation (:position (last target))) (/ (* 3 pi) 2))
                    (direction->rotation (:position (first target))))))
                (+ (direction->rotation (:position (first zap))) pi))}
           (if (> (* (max (:spawn-probability g) 0.01)
                       (/ (:energy p) 100))
                    (rand))
               {:spawn [((:spawn-function p) p)]}
               {})))))


(defn chaser2 []
  (merge (active)
         {:chaser true
          :team2 true
          :player true
          :proposal-function chaser2-proposals
          :memory {:genome {:random-urge (rand-urge)
                            :obs-urge (rand-urge)
                            :beater-urge (rand-urge)
                            :spawn-probability (rand-probability)}}
          
          :color [0 255 0]
           :spawn-function #(merge %
                                  {:velocity [(* 5 (- (rand) 0.5)) (* 5 (- (rand) 0.5))]
                                   :rotation (* two-pi (rand))
                                   ;; position will be relative to position of parent
                                   :position [(- (rand-int 3) 1) (- (rand-int 3) 1)]
                                   :memory {:genome (chasemutate (:genome (:memory %)))}})}))

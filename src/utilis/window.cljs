(ns utilis.window)

(defn scroll-offset
  []
  (let [doc (.-documentElement js/document)]
    {:left (- (or (.-pageXOffset js/window) (.-scrollLeft doc))
              (or (.-clientLeft doc) 0))
     :top (- (or (.-pageYOffset js/window) (.-scrollTop doc))
             (or (.-clientTop doc) 0))}))

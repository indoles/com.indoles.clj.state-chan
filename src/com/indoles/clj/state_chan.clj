(ns com.indoles.clj.state-chan
  (:require [clojure.core.async :as async]))

(defn init
  ([ch init-state]
     (async/go-loop [req ch
                     state init-state
                     item (async/<! req)]
                    (if (= :stop item)
                      (do
                        (async/close! req))
                      (let [next-state (item state)]
                        (recur req next-state (async/<! req)))))
     ch)
  ([init-state] (init (async/chan) init-state))
  ([] (init {})))

(defn send-msg [ch msg]
  (async/go (async/>! ch msg)))

(defn receive-msg [ch]
  (async/<!! ch))

(defn stop [ch]
  (send-msg ch :stop)
  nil)

(defn respond-to
  ([ch rep-ch f]
     (send-msg ch (fn [item] (let [ret (f item)]
                              (async/go (async/>! rep-ch ret))
                              ret)))
     (receive-msg rep-ch))
  ([ch f]
     (let [rep-ch (async/chan)
           ret (respond-to ch rep-ch f)]
       (async/close! rep-ch)
       ret)))

(defn state [ch]
  (respond-to ch identity))

(ns com.indoles.clj.state-chan
  (:require [clojure.core.async :as async]))

(defn init
  ([req-ch repl-ch init-state]
     (async/go-loop [req req-ch
                     repl repl-ch
                     state init-state
                     item (async/<! req)]
                    (if (= :stop item)
                      (do
                        (async/close! repl)
                        (async/close! req))
                      (let [ret (-> state item)]
                        (when ret (async/>! repl ret))
                        ; update state with a new status from
                        ; function invocation, only if non-nil
                        (recur req repl (if ret ret state) (async/<! req)))))
     {:req-ch req-ch :repl-ch repl-ch})
  ([init-state] (init (async/chan) (async/chan) init-state))
  ([] (init {})))

(defn send-msg [{:keys [req-ch repl-ch]} msg]
  (async/go (async/>! req-ch msg)))

(defn receive-msg [{:keys [reg-ch repl-ch]}]
  (async/<!! repl-ch))

(defn stop [machine]
  (send-msg machine :stop)
  nil)

(defn respond-to [machine f]
  (send-msg machine f)
  (receive-msg machine))

(defn state [machine]
  (respond-to machine identity))

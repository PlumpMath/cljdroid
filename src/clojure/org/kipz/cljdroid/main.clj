(ns org.kipz.cljdroid.main
  (:require [neko.activity :refer [defactivity set-content-view!]]
            [neko.debug :refer [*a]]
            [neko.notify :refer [toast]]
            [neko.find-view :refer [find-view]]
            [neko.threading :refer [on-ui]]
            [neko.listeners.view :refer :all]
            [neko.log :as log])
  (:require [clojure.core.async
             :as a
             :refer [>! <! >!! <!! go chan go-loop put! tap mult close! thread
                     alts! alts!! timeout]])
  (:gen-class))

(defn by-id
  "Fetch a view from a context (view/activity)"
  [activity view-id]
  (find-view activity view-id))

(defn set-status!
  [activity status]
  (log/d "Setting status to: " status)
  (on-ui
    (.setText (by-id activity ::status-text) (str status))))

(defn clicks->chan
  "Given a target View and return a channel of
  observed clicks. Can supply the channel to receive events as third
  optional argument."
  ([view] (clicks->chan view (chan)))
  ([view c]
   (.setOnClickListener view
     (on-click-call (fn [view]
                        (>!! c view))))                     ;transducers change these :/
   c))

(defn magic-sequence
  "Set up magic key sequence"
  [activity]
  (log/d "Setting up magic sequence...")
  (let [a                    (clicks->chan (by-id activity ::button-a) (chan 1 (map (constantly :a))))
        b                    (clicks->chan (by-id activity ::button-b) (chan 1 (map (constantly :b))))
        combination-max-time 5000
        secret-combination   [:a :a :b :b :a :a]]
    (go-loop [correct-clicks []
              timer (timeout combination-max-time)]
      (let [[val channel] (alts! [a b timer])
            clicks (conj correct-clicks val)]
        (cond
          (= channel timer) (do
                                (set-status! activity "You're not fast enough, try again!")
                                (recur [] (timeout combination-max-time)))
          (= clicks secret-combination) (do
                                          (set-status! activity "Combination unlocked!")
                                          (recur [] (timeout combination-max-time)))
          (and (= val (first secret-combination)) (zero? (count correct-clicks))) (do
                                                                                    (set-status! activity clicks)
                                                                                    (recur clicks (timeout combination-max-time)))
          (= val (nth secret-combination (count correct-clicks))) (do
                                                                    (set-status! activity clicks)
                                                                    (recur clicks timer))
          :else
          (do
            (set-status! activity clicks)
            (recur [] timer)))))))


(defactivity org.kipz.cljdroid.StartingActivity
  :key :main
  :on-start (fn [activity] (magic-sequence activity))
  :on-create
  (fn [this bundle]
    (on-ui
      (set-content-view! this
        [:linear-layout {:orientation :vertical}
         [:text-view {:text "Enter AABBAA within 5 seconds!"}]
         [:button {:text "A" :id ::button-a}]
         [:button {:text "B" :id ::button-b}]
         [:text-view {:id ::status-text}]]))))

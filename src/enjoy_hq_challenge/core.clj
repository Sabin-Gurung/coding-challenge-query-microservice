(ns enjoy-hq-challenge.core
  (:require
    [enjoy-hq-challenge.api.handler :refer [make-app]]
    [org.httpkit.server :as ser])
  (:gen-class))

(defonce server (atom nil))

(defn stop-server! []
  (when @server
    (@server)))

(defn start-server! [handler port]
  (stop-server!)
  (reset! server (ser/run-server handler {:port port})))

(defn -main
  "Entry point to run the program"
  [& args]
  (let [[p & _] args
        port (if (empty? p) 3000 (Integer/parseInt p))]
    (println "server started on port " port)
    (start-server! (make-app) port)))

(comment
  (string? nil)
  (-main "300")
  (start-server! (make-app) 3000)
  (stop-server!)
  )

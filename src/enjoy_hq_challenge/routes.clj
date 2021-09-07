(ns enjoy-hq-challenge.routes
  )

(def ping-routes
  ["/health-check" {:get {:handler (fn [_]
                                     {:status  200
                                      :body    {:message "up"}
                                      :headers {}})
                          }}])

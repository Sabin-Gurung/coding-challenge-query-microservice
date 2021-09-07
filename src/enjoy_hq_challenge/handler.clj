(ns enjoy-hq-challenge.handler
  (:require
    [reitit.ring :as ring]
    ))

(defn make-app []
  (ring/ring-handler
    (ring/router
      [
       ["/health-check" {:get {:handler (fn [_]
                                          {:status 200
                                           :body {:message "up"}
                                           :headers {}})
                               }}]]
      )
    (ring/create-default-handler)
    ))


(comment
  (def app (make-app))

  (app {:request-method :get :uri "/health-check"})
  (app {:request-method :get :uri "/lala"})
  )

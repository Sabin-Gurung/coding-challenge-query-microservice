(ns enjoy-hq-challenge.handler
  (:require
    [reitit.ring :as ring]
    ))

(defn default-handlers []
  (ring/create-default-handler
    {:not-found          (constantly {:status 404 :body {:message "Endpoint not found"}})
     :method-not-allowed (constantly {:status 405 :body {:message "Method not allowed"}})
     :not-acceptable     (constantly {:status 406 :body {:message "Request not acceptable"}})}))

(defn make-app []
  (ring/ring-handler
    (ring/router
      [
       ["/health-check" {:get {:handler (fn [_]
                                          {:status  200
                                           :body    {:message "up"}
                                           :headers {}})
                               }}]]
      )
    (default-handlers)
    ))


(comment
  (def app (make-app))

  (app {:request-method :get :uri "/health-check"})
  (app {:request-method :post :uri "/health-check"})
  (app {:request-method :get :uri "/lala"})
  )

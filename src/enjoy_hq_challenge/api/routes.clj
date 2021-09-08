(ns enjoy-hq-challenge.api.routes
  (:require
    [schema.core :as s]
    [enjoy-hq-challenge.use-cases.users :as user-use-case]
    [ring.util.response :as rs]
    )
  )

(def ping-routes
  ["/health-check" {:get {:summary "Status of the service"
                          :handler (fn [_]
                                     {:status  200
                                      :body    {:message "up"}
                                      :headers {}})
                          }}])

(def user-routes
  ["/sign-up" {:post {:summary    "Create new user"
                      :parameters {:body {:username s/Str
                                          :password s/Str}}
                      :handler    (fn [{{body :body} :parameters}]
                                    (rs/response (user-use-case/create-user body)))}}
   ])

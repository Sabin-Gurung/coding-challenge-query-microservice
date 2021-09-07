(ns enjoy-hq-challenge.routes
  (:require [schema.core :as s])
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
                      :handler    (fn [{{{:keys [username password]} :body} :parameters}]
                                    {:status  200
                                     :body    {:username username
                                               :password password}
                                     :headers {}})
                      }}])

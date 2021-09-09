(ns enjoy-hq-challenge.api.routes
  (:require
    [enjoy-hq-challenge.api.auth :as auth]
    [enjoy-hq-challenge.api.schemas :as sc]
    [enjoy-hq-challenge.use-cases.users :as user-use-case]
    [enjoy-hq-challenge.use-cases.documents :as doc-use-case]
    [ring.util.response :as rs]
    [schema.core :as s]
    ))

(def health-routes
  ["" {:swagger {:tags ["heath"]}}
   ["/health-check" {:get {:summary "Status of the service"
                           :handler (fn [_]
                                      {:status  200
                                       :body    {:message "up"}
                                       :headers {}})}}]])

(def user-routes
  ["" {:swagger {:tags ["authentication"]}}
   ["/sign-up" {:post {:summary    "Create new user"
                       :parameters {:body sc/User}
                       :handler    (fn [{{body :body} :parameters}]
                                     (rs/response (user-use-case/create-user body)))}}]

   ["/authenticate" {:post {:summary    "Login user"
                            :parameters {:body sc/User}
                            :handler    (fn [{{body :body} :parameters}]
                                          (if-let [payload (user-use-case/validate-user body)]
                                            (rs/response {:token (auth/create-token payload)})
                                            {:status 401 :body {:error "Invalid credential"}}))}}]])

(def document-routes
  ["" {:middleware [auth/wrap-jwt-authentication auth/auth-middleware]
       :swagger    {:tags ["document [private]"]}}
   ["/_index" {:post {:summary    "Post a new document"
                      :parameters {:body sc/Document}
                      :handler    (fn [{{body :body}         :parameters
                                        {username :username} :identity}]
                                    (rs/response (doc-use-case/create username body)))}}]
   ["/_index/:id" {:get {:summary    "Get a document"
                         :parameters {:path {:id s/Int}}
                         :handler    (fn [{{{id :id} :path}     :parameters
                                           {username :username} :identity}]
                                       (if-let [doc (doc-use-case/fetch username id)]
                                         (rs/response doc)
                                         (rs/not-found {:error "document not found"})))}
                   :del {:summary    "Delete a document"
                         :parameters {:path {:id s/Int}}
                         :handler    (fn [{{{id :id} :path}     :parameters
                                           {username :username} :identity}]
                                       (if-let [doc (doc-use-case/fetch username id)]
                                         (rs/response doc)
                                         (rs/not-found {:error "document not found"})))}
                   }]
   ])

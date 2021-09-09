(ns enjoy-hq-challenge.api.auth
  (:require
    [buddy.auth :refer [authenticated?]]
    [buddy.auth.backends :as backends]
    [buddy.auth.middleware :refer [wrap-authentication]]
    [buddy.sign.jwt :as jwt]
    [buddy.sign.util :refer [to-timestamp]]
    [clj-time.core :as t]
    [config.core :refer [env]]
    ))

(def auth-config (:auth env))

(def jwt-secret (:jwt-secret auth-config))
(def token-duration-seconds (:token-duration-seconds auth-config))

(defn exp-time []
  (-> (t/now)
      (t/plus (t/seconds token-duration-seconds))
      to-timestamp))

(defn create-token [payload]
  (jwt/sign payload jwt-secret {:exp (exp-time)}))

(defn wrap-jwt-authentication
  [handler]
  (wrap-authentication handler (backends/jws {:secret jwt-secret})))

(defn auth-middleware [handler]
  (fn [request]
    (if (authenticated? request)
      (handler request)
      {:status 401 :body {:error "Unauthorized or expired token"}})))

(comment
  (def user {:username "username"})
  (def token (create-token user))
  (def app (wrap-jwt-authentication identity))
  (app {:headers {"authorization" (str "Token " token)}})

  )

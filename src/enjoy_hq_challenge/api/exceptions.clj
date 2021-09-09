(ns enjoy-hq-challenge.api.exceptions
  (:require
    [reitit.ring.middleware.exception :as r-ex]
    [ring.util.response :as resp]
    [ring.util.response :as res])
  )

(defn error-body [exception req type]
  (let [[message detail] ((juxt ex-message ex-data) exception)]
    {:body {:detail detail
            :title  message
            :type   type
            :uri    (:uri req)}}))

(defn -not-found-handler [exception req]
  (-> (error-body exception req "problem:no-resource")
      res/not-found))

(defn -already-exist-handler [exception req]
  (-> (error-body exception req "problem:resource-already-exists")
      (assoc :status 422)))

(defn -unauthorized-handler [exception req]
  (-> (error-body exception req "problem:invalid-credentials")
      (assoc :status 401)))

(defn app-exception-handlers []
  {:app/no-resource             -not-found-handler
   :app/resource-already-exists -already-exist-handler
   :app/invalid-credentials     -unauthorized-handler
   }
  )

(defn exception-500-handler [exception req]
  {:status  500
   :body    {:detail (.getMessage exception)
             :title  "There was an unknown error"
             :type   "problem:internal-server-error"
             :uri    (:uri req)}
   :headers {}})

(def middleware
  (r-ex/create-exception-middleware
    (merge
      r-ex/default-handlers
      (app-exception-handlers)
      {::r-ex/default exception-500-handler})))


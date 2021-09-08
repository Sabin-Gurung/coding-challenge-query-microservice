(ns enjoy-hq-challenge.api.handler
  (:require
    [enjoy-hq-challenge.api.routes :as routes]
    [muuntaja.core :as m]
    [reitit.coercion.schema]
    [reitit.ring :as ring]
    [reitit.ring :as ring]
    [reitit.ring.coercion :as coercion]
    [reitit.ring.middleware.exception :as exception]
    [reitit.ring.middleware.muuntaja :as muuntaja]
    [reitit.ring.middleware.parameters :as parameters]
    [reitit.swagger :as swagger]
    [reitit.swagger-ui :as swagger-ui]
    ))

(defn swagger-routes []
  ["" {:no-doc true}
   ["/swagger.json" {:get (swagger/create-swagger-handler)}]
   ["/api-docs/*" {:get (swagger-ui/create-swagger-ui-handler)}]])

(defn make-app []
  (ring/ring-handler
    (ring/router
      [(swagger-routes)
       routes/ping-routes
       routes/user-routes
       ]
      {:data {:coercion   reitit.coercion.schema/coercion
              :muuntaja   m/instance
              :middleware [parameters/parameters-middleware
                           muuntaja/format-negotiate-middleware
                           muuntaja/format-response-middleware
                           exception/exception-middleware
                           muuntaja/format-request-middleware
                           coercion/coerce-response-middleware
                           coercion/coerce-request-middleware]
              }})
    (ring/create-default-handler)))

(comment
  (def app (make-app))

  (app {:request-method :get :uri "/health-check"})
  (app {:request-method :post :uri "/health-check"})
  (app {:request-method :get :uri "/lala"})
  )

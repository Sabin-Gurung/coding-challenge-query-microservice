(ns enjoy-hq-challenge.api.schemas
  (:require
    [schema.core :as s]))

(s/defschema User
  {:username s/Str
   :password s/Str})

(s/defschema Document
  {:title   s/Str
   :content s/Str})

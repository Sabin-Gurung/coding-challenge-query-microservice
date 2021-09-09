(ns enjoy-hq-challenge.api.schemas
  (:require
    [schema.core :as s]))

(s/defschema User
  {:username s/Str
   :password s/Str})

(def date-regex #"\d{4}-\d{2}-\d{2}(T\d{2}:\d{2}:\d{2}Z)?")

(s/defschema Document
  {(s/optional-key :id)         s/Int
   :title                       s/Str
   :content                     s/Str
   (s/optional-key :created_at) date-regex
   (s/optional-key :updated_at) date-regex})

(comment
  (s/validate Document {:id         2
                        :title      "hello"
                        :content    "content"
                        :created_at "2021-03-04T12:32:45Z"
                        :updated_at "2021-03-04"})

  )

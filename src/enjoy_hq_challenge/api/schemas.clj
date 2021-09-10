(ns enjoy-hq-challenge.api.schemas
  (:require
    [schema.core :as s]))

(s/defschema User
  {:username s/Str
   :password s/Str})

(def date-regex #"^\d{4}-\d{2}-\d{2}(T\d{2}:\d{2}:\d{2}Z)?$")

(s/defschema Document
  {(s/optional-key :id)         s/Int
   (s/optional-key :title)      s/Str
   (s/optional-key :content)    s/Str
   (s/optional-key :created_at) date-regex
   (s/optional-key :updated_at) date-regex})

(def date-fields #{"created_at" "updated_at"})
(def text-fields #{"title" "content"})

(s/defschema TextFilter
  {:field       (apply s/enum text-fields)
   :filter_type (s/enum "exact_match" "starts_with" "substring")
   :match       s/Str})

(s/defschema DateFilter
  {:field               (apply s/enum date-fields)
   :from                date-regex
   (s/optional-key :to) date-regex})

(def Filter
  (s/conditional
    #(date-fields (:field %)) DateFilter
    #(text-fields (:field %)) TextFilter))

(s/defschema Query
  {:filters  [Filter]
   :order_by (apply s/enum date-fields)})

(comment
  (s/validate Document {:id         2
                        :title      "hello"
                        :content    "content"
                        :created_at "2021-03-04T12:32:45Z"
                        :updated_at "2021-03-04"})

  (date-fields "")
  (s/validate Filter {:field "updated_at"
                      :from  "2021-12-12T12:13:12Z"})
  (s/validate Filter {:field       "title"
                      :filter_type "exact_match"
                      :match       "content"})

  (s/validate Query {:order_by "updated_at"
                     :filters  [{:field       "title"
                                 :filter_type "exact_match"
                                 :match       "hello"}
                                {:field "created_at"
                                 :from  "2012-12-12"
                                 :to    "2012-12-12"
                                 }
                                {:field "created_at"
                                 :from  "2012-12-12"
                                 :to    "2012-12-12"
                                 }
                                ]
                     })
  )

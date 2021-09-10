(ns enjoy-hq-challenge.data
  (:require [clojure.test :refer :all])
  )

(def a-name "sabin")

(def a-username {:username a-name})

(def a-user {:username a-name
             :password "passwd"})

(def a-document
  {:username   (:username a-user)
   :title      "Banana"
   :content    "I love banana. Tasty and awesome. My favorite food"
   :created_at "2020-01-01T00:00:00Z"
   :updated_at "2021-12-12T00:00:00Z"})

(def b-document
  {:username   (:username a-user)
   :title      "Bandana"
   :content    "Bandana are awesome they are so fashionable"
   :created_at "2020-03-01T12:30:30Z"
   :updated_at "2021-10-01T12:00:00Z"})

(def order-a-b {:order_by "created_at"
                :filters  []})

(def order-b-a {:order_by "updated_at"
                :filters  []})

(def exact-title-banana {:filter_type "exact_match"
                         :field       "title"
                         :match       "banana"})

(def starts-title-ban {:filter_type "starts_with"
                       :field       "title"
                       :match       "ban"})

(def substring-title-dana {:filter_type "substring"
                           :field       "title"
                           :match       "dana"})

(def substring-content-awesome {:filter_type "substring"
                                :field       "content"
                                :match       "awesome"})

(def in-a {:field "created_at"
           :from  "2020-01-01"
           :to    "2020-03-01"})

(def in-a-b {:field "created_at"
             :from  "2020-01-01"})

(def a-update-document
  (assoc a-document :id 1))

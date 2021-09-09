(ns enjoy-hq-challenge.data
  (:require [clojure.test :refer :all])
  )

(def a-user {:username "sabin"
             :password "passwd"})

(def a-document
  {:username   (:username a-user)
   :title      "hello"
   :content    "content"
   :created_at "2021-03-04T12:32:45Z"
   :updated_at "2021-03-04T12:00:00Z"})

(def b-document
  {:username   (:username a-user)
   :title      "hello b document"
   :content    "content b"
   :created_at "2021-03-04T12:32:45Z"
   :updated_at "2021-03-04T12:00:00Z"})

(def a-update-document
  (assoc a-document :id 1))

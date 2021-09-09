(ns enjoy-hq-challenge.use-cases.documents
  (:require [enjoy-hq-challenge.dao.dao :as dao])
  )


(defn create [username body]
  (let [doc (dao/insert-document! (assoc body :username username))]
    {:status "ack"
     :id     (:generated_key doc)}))

(defn fetch [username id]
  (let [body {:username username
              :id       id}]
    (dao/get-document body)))

(defn delete [username id]
  (let [body {:username username
              :id       id}]
    (dao/del-document! body)))

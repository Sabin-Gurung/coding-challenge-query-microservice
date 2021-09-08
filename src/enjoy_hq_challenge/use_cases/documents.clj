(ns enjoy-hq-challenge.use-cases.documents
  (:require [enjoy-hq-challenge.dao.dao :as dao])
  )


(defn create [username body]
  (let [doc (dao/insert-document! (assoc body :username username))]
    {:status "ack"
     :id     (:generated_key doc)}))
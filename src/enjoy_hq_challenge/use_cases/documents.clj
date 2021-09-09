(ns enjoy-hq-challenge.use-cases.documents
  (:require [enjoy-hq-challenge.dao.dao :as dao])
  )


(defn- should-update? [doc]
  (:id doc))

(defn- identifier [doc username]
  {:id       (:id doc)
   :username username})

(defn- map-generated-id [{generated_key :generated_key}]
  {:id generated_key})

(defn save [username doc]
  (let [db-body (-> doc
                    (dissoc :id)
                    (assoc :username username))]
    (-> (if (should-update? doc)
          (dao/update-document! db-body (identifier doc username))
          (map-generated-id (dao/insert-document! db-body)))
        (assoc :status "ack"))))

(defn fetch [username id]
  (if-let [doc (dao/get-document {:username username :id id})]
    doc
    (throw (ex-info "document does not exist" {:type :app/no-resource}))))

(defn delete [username id]
  (let [body {:username username
              :id       id}]
    (-> (dao/del-document! body)
        (assoc :status "ack"))))

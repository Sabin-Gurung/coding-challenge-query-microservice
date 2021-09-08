(ns enjoy-hq-challenge.use-cases.users
  (:require
    [enjoy-hq-challenge.dao.dao :as dao]
    )
  )


(defn create-user [user]
  (let [user-exist? (dao/get-user user)]
    (if user-exist?
      {:status "ko"}
      (do
        (dao/create-user! user)
        {:status "ack"}))))

(defn validate-user [user]
  (let [db-user (dao/get-user user)]
    (when db-user
      (select-keys db-user [:username]))))

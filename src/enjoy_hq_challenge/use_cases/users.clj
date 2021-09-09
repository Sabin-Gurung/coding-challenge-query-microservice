(ns enjoy-hq-challenge.use-cases.users
  (:require
    [enjoy-hq-challenge.dao.dao :as dao]
    )
  )


(defn create-user [user]
  (let [user-exist? (dao/get-user user)]
    (when user-exist? (throw (ex-info "user already exists" {:type :app/resource-already-exists})))
    (dao/create-user! user)
    {:status "ack"}))

(defn validate-user [user]
  (if-let [db-user (dao/get-user user)]
    (select-keys db-user [:username])
    (throw (ex-info "invalid credentials" {:type :app/invalid-credentials}))))

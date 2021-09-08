(ns enjoy-hq-challenge.dao.dao
  (:require
    [enjoy-hq-challenge.database :refer [execute execute-one]]
    [honeysql.core :as h]
    [honeysql.helpers :as hh]
    )
  )

(defn get-user [{:keys [username]} & tx]
  (-> (hh/select :*)
      (hh/from :users)
      (hh/where [:= :username username])
      h/format
      (execute-one (first tx))))

(defn create-user! [user & tx]
  (-> (hh/insert-into :users)
      (hh/values [user])
      h/format
      (execute-one (first tx))))

(comment
  (get-user {:username "bb"})
  (create-user! {:username "there" :password "hello"})
  )

(ns enjoy-hq-challenge.dao.dao
  (:require
    [enjoy-hq-challenge.database :refer [execute execute-one]]
    [honeysql.core :as h]
    [honeysql.helpers :as hh]
    )
  )

(defn- where [sql-map where-map]
  "create honey sql where clause from map"
  (->> where-map
       (filter (fn [[k v]] (some? v)))
       (map (fn [[k v]] [:= k v]))
       (apply (partial hh/where sql-map))))

(defn get-user [user & tx]
  (-> (hh/select :*)
      (hh/from :users)
      (where user)
      h/format
      (execute-one (first tx))))

(defn create-user! [user & tx]
  (-> (hh/insert-into :users)
      (hh/values [user])
      h/format
      (execute-one (first tx))))

(defn insert-document! [doc & tx]
  (-> (hh/insert-into :documents)
      (hh/values [doc])
      h/format
      (execute-one (assoc tx :return-keys true))))

(comment
  (get-user {:username "bb" :password nil})
  (create-user! {:username "there" :password "hello"})
  (-> (hh/select :*)
      (hh/from :users)
      (hh/where [:= :username])
      (where {:username "sabin"
              :password "passowrd"})
      ;(where {:username "sabin"
      ;        :password nil})
      h/format
      )

  (map->where {} {:username "sabin" :password "asdf"})
  (map->where {} {:username "sabin" :password nil})
  )

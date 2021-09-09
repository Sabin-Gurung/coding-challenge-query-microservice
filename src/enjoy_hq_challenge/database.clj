(ns enjoy-hq-challenge.database
  (:require [next.jdbc :as jdbc]
            [next.jdbc.result-set :as rs]
            [honeysql.core :as h]
            [honeysql.helpers :as hh]
            [config.core :refer [env]]
            )
  )

(def db-config (:db env))

(def my-db (jdbc/get-datasource db-config))

(defn- target [transaction]
  (if transaction
    transaction
    my-db))

(defn- options [return-keys]
  {:builder-fn  rs/as-unqualified-lower-maps
   :return-keys return-keys})

(defn execute [query {:keys [transaction return-keys]}]
  (jdbc/execute! (target transaction) query (options return-keys)))

(defn execute-one [query {:keys [transaction return-keys]}]
  (jdbc/execute-one! (target transaction) query (options return-keys)))

(comment
  (-> (hh/insert-into :users)
      (hh/values [#_{:username "hello" :password "aa"}
                  {:username "bb" :password "there"}])
      (h/format)
      ;vec
      ;(->> (jdbc/execute! my-db))
      (execute {})
      )

  (-> (hh/update :users)
      (hh/set0 {:password "oh"})
      (hh/where [:= :username "hello"])
      (h/format)
      ;vec
      ;(->> (jdbc/execute! my-db))
      (execute)
      )

  (-> (hh/insert-into :documents)
      (hh/values [{:username "bb" :title "wehrkjla" :content "contetndsfkjsadfads"}])
      h/format
      (execute-one {:return-keys true})
      )

  (-> (hh/delete-from :users)
      h/format
      (execute-one {:return-keys false})
      )

  (-> (hh/select :*)
      (hh/from :users)
      (h/format)
      ;vec
      ;(->> (jdbc/execute! my-db))
      (execute)
      )

  (jdbc/execute! my-db ["CREATE TABLE users (
                            username varchar(255) not null,
                            password varchar(255) not null,
                            primary key (username)
                        );"])
  )
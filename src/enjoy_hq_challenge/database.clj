(ns enjoy-hq-challenge.database
  (:require [next.jdbc :as jdbc]
            [next.jdbc.result-set :as rs]
            [honeysql.core :as h]
            [honeysql.helpers :as hh]
            )
  )

(def db-spec {:dbtype   "mysql"
              :dbname   "enjoy_hq_dev"
              :user     "root"
              :password "qwerasdf"
              :host     "localhost"
              :port     "3307"
              })

(def my-db (jdbc/get-datasource db-spec))

(defn execute [query]
  (jdbc/execute! my-db query {:builder-fn rs/as-unqualified-maps}))

(comment
  (-> (hh/insert-into :users)
      (hh/values [#_{:username "hello" :password "aa"}
                  {:username "bb" :password "there"}])
      (h/format)
      ;vec
      ;(->> (jdbc/execute! my-db))
      (execute)
      )

  (-> (hh/update :users)
      (hh/set0 {:password "oh"})
      (hh/where [:= :username "hello"])
      (h/format)
      ;vec
      ;(->> (jdbc/execute! my-db))
      (execute)
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
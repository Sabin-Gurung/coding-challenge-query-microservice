(ns enjoy-hq-challenge.database
  (:require [next.jdbc :as jdbc]
            [next.jdbc.result-set :as rs]
            [honeysql.core :as h]
            [honeysql.helpers :as hh]
            )
  )

(def db-config {:classname "com.mysql.jdbc.Driver"
                :dbtype    "mysql"
                :user      "root"
                :password  "qwerasdf"
                :host      "localhost"
                :port      "3307"
                :dbname    "enjoy_hq_dev"
                :subname   "//localhost:3307/enjoy_hq_dev"})

(def my-db (jdbc/get-datasource db-config))

(defn execute [query transaction]
  (if transaction
    (jdbc/execute! transaction query {:builder-fn rs/as-unqualified-maps})
    (jdbc/execute! my-db query {:builder-fn rs/as-unqualified-maps})))

(defn execute-one [query transaction]
  (if transaction
    (jdbc/execute-one! transaction query {:builder-fn rs/as-unqualified-maps})
    (jdbc/execute-one! my-db query {:builder-fn rs/as-unqualified-maps})))

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
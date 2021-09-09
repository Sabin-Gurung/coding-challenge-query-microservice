(ns enjoy-hq-challenge.dao.dao
  (:require
    [enjoy-hq-challenge.database :refer [execute execute-one]]
    [honeysql.core :as h]
    [honeysql.helpers :as hh]
    [clj-time.jdbc]
    [clj-time.core :as t]
    [clj-time.coerce :refer [to-sql-time]]
    [clj-time.format :as tf]
    [clj-time.coerce :as tc])
  )

(def from-time-formatter
  (tf/formatter t/utc "YYYY-MM-dd" "YYYY-MM-dd'T'HH:mm:ssZ"))

(def to-time-formatter
  (tf/formatter :date-time-no-ms))

(defn str->sql-time [time-str]
  (-> (tf/parse from-time-formatter time-str)
      (tc/to-sql-time)))

(defn sql-time->str [sql-time]
  (-> sql-time
      tc/from-sql-time
      (->> (tf/unparse to-time-formatter))))

(defn- where [sql-map where-map]
  "create honey sql where clause from map"
  (->> where-map
       (filter (fn [[k v]] (some? v)))
       (map (fn [[k v]] [:= k v]))
       (apply (partial hh/where sql-map))))

(defn get-user [user]
  (-> (hh/select :*)
      (hh/from :users)
      (where user)
      h/format
      execute-one))

(defn create-user! [user]
  (-> (hh/insert-into :users)
      (hh/values [user])
      h/format
      execute-one))

(defn- prepare-doc [doc]
  (-> doc
      (update :updated_at #(some-> % str->sql-time))
      (update :created_at #(some-> % str->sql-time))))

(defn insert-document! [doc]
  (-> (hh/insert-into :documents)
      (hh/values [(prepare-doc doc)])
      h/format
      (execute-one true)))

(defn update-document! [doc cond-map]
  (-> (hh/update :documents)
      (hh/set0 (prepare-doc doc))
      (where cond-map)
      h/format
      execute-one))

(defn get-document [doc]
  (-> (hh/select :*)
      (hh/from :documents)
      (where doc)
      h/format
      execute-one
      (some-> (update :updated_at sql-time->str)
              (update :created_at sql-time->str))))

(defn del-document! [doc]
  (-> (hh/delete-from :documents)
      (where doc)
      h/format
      execute-one))







(comment
  (del-document! {:id 20})
  (get-document {:id 21})
  (get-document {:username "stringa" :id 3})
  (tf/show-formatters)
  (-> (tf/parse (tf/formatter
                  t/utc "YYYY-MM-dd" "YYYY-MM-dd'T'HH:mm:ssZ") "2021-03-21T12:30:23Z")
      (tc/to-sql-time))

  (def dd {:updated_at "2021-12-20"})
  (cond-> dd (:created_at dd) (update :updated_at str->sql-time))
  (-> {:updated_at "2021-12-20"}
      (update :updated_at #(some-> % str->sql-time)))

  (insert-document!
    {:username "there"
     :title    "jeje"
     :content  "there"
     ;:created_at (tc/to-sql-time (t/now))
     ;:updated_at (str->sql-time "")
     })

  (-> (get-document {:id 10})
      (update :created_at sql-time->str)
      (update :updated_at type)
      )
  (-> (hh/select :*)
      (hh/from :documents)
      (h/format)
      (execute nil))

  (update-document! {:title "banana lover lover"}
                    {:username "tom"
                     :id       27})

  (get-document {:id 10})
  (get-user {:username "bb" :password nil})
  (create-user! {:username "there" :password "hello"})
  (-> (hh/select :*)
      (hh/from :users)
      (hh/where [:= :username])
      (where {:username "sabin"
              :password "passowrd"})
      h/format)

  (map->where {} {:username "sabin" :password "asdf"})
  (map->where {} {:username "sabin" :password nil})
  )

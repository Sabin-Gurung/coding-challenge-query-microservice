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

(defn- document->sql [doc]
  (-> doc
      (update :updated_at #(some-> % str->sql-time))
      (update :created_at #(some-> % str->sql-time))))

(defn- sql->document [doc]
  (-> doc
      (update :updated_at sql-time->str)
      (update :created_at sql-time->str)))

(defn insert-document! [doc]
  (-> (hh/insert-into :documents)
      (hh/values [(document->sql doc)])
      h/format
      (execute-one true)))

(defn update-document! [doc cond-map]
  (-> (hh/update :documents)
      (hh/set0 (document->sql doc))
      (where cond-map)
      h/format
      execute-one))

(defn get-document [doc]
  (-> (hh/select :*)
      (hh/from :documents)
      (where doc)
      h/format
      execute-one
      (some-> sql->document)))

(defn del-document! [doc]
  (-> (hh/delete-from :documents)
      (where doc)
      h/format
      execute-one))

(defn where-text [sql {:keys [match field filter_type]}]
  (let [like (condp = filter_type
               "exact_match" match
               "starts_with" (str match "%")
               "substring" (str "%" match "%"))]
    (hh/merge-where sql [:like (keyword field) like])))

(defn where-date [sql {:keys [field from to]}]
  (let [fld (keyword field)]
    (-> (hh/merge-where sql [:>= fld from])
        (cond-> to (hh/merge-where [:<= fld to])))))

(defn apply-filter [sql filter]
  (let [text-filter? :match]
    (if (text-filter? filter)
      (where-text sql filter)
      (where-date sql filter))))

(defn query-document [doc {:keys [filters order_by]}]
  (-> (hh/select :*)
      (hh/from :documents)
      (hh/order-by [(keyword order_by)])
      (where doc)
      ((partial reduce apply-filter) filters)
      (h/format)
      (execute)
      (->> (map sql->document))))






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
    {:username   "sabin"
     :title      "baby"
     :content    "there"
     :created_at "2021-12-10"
     :updated_at "2021-12-12"
     })

  (-> (get-document {:id 10})
      (update :created_at sql-time->str)
      (update :updated_at type)
      )

  (def query
    [
     {:filter_type "exact"
      :from        ""
      :field       ""
      }
     ]
    )
  (where-text {:match       "th"
               :filter_type "substring"
               :field       "content"})
  (where-date {:from  "2021-09-10T13:53:40Z"
               :field "updated_at"})

  (-> (hh/select :*)
      (hh/from :documents)
      (hh/order-by [(keyword "updated_at")])
      (where {:username "username"})
      ((partial reduce apply-filter) [{:match       "th"
                                       :filter_type "substring"
                                       :field       "content"}
                                      #_{:match       "th"
                                         :filter_type "starts_with"
                                         :field       "title"}
                                      {:from  "2021-08-10T13:53:40Z"
                                       :field "updated_at"}
                                      {:from  "2021-08-10T13:53:40Z"
                                       :field "created_at"
                                       :to    "2021-12-12"}])
      (h/format)
      ;(execute)
      )

  (-> (hh/select :*)
      (hh/from :documents)
      ;(hh/order-by [:created_at])
      (where-text {:match       "th"
                   :filter_type "substring"
                   :field       "content"})
      (where-date {:from  "2021-09-10T13:53:40Z"
                   :field "updated_at"})

      ;(hh/merge-where [:< :created_at "2021-09-10T13:55:00Z"])
      (h/format)
      (execute)
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

(ns enjoy-hq-challenge.migrate
  (:require
    [migratus.core :as migratus]
    [enjoy-hq-challenge.database :refer [db-config]]
    )
  )

(def config {:store                :database
             :migration-dir        "migrations/"
             :migration-table-name "_migrations"
             :db                   (assoc db-config :subprotocol (:dbtype db-config))})

(defn run-migration []
  (println "Pending migrations : " (migratus/pending-list config))
  (migratus/migrate config)
  (println "Pending migrations : " (migratus/pending-list config))
  (println "Completed migrations"))

(comment
  (migratus/pending-list config)
  (migratus/migrate config)

  (migratus/up config 20210910204035)
  (migratus/down config 20210910204035)

  (migratus/create config "create users table")
  (migratus/create config "create docs table")
  (migratus/create config "add test data")
  )
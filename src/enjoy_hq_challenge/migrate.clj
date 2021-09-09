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

(comment
  (migratus/pending-list config)
  (migratus/migrate config)

  (migratus/up config 20210908213657)
  (migratus/down config 20210908213657)

  (migratus/create config "create users table")
  (migratus/create config "create docs table")
  )
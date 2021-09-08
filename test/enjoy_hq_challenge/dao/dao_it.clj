(ns enjoy-hq-challenge.dao.dao-it
  (:require [clojure.test :refer :all]
            [enjoy-hq-challenge.dao.dao :as dao]
            [next.jdbc :as jdbc]
            [enjoy-hq-challenge.database :as db]
            [enjoy-hq-challenge.data :as data]
            ))

(deftest create-and-fetch-user
  (jdbc/with-transaction
    [tx db/my-db {:rollback-only true}]
    (let [op {:transaction tx}]

      ; given no user
      (is (= nil (dao/get-user data/a-user op)))

      ; when create user
      (dao/create-user! data/a-user op)

      ; user should exist
      (is (= data/a-user (dao/get-user data/a-user op))))
    ))

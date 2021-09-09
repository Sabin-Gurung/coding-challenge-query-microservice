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

(deftest create-insert-document
  (jdbc/with-transaction
    [tx db/my-db {:rollback-only true}]
    (let [op {:transaction tx}]

      (dao/create-user! data/a-user op)
      (let [{generated_key_a :generated_key} (dao/insert-document! data/a-document op)
            {generated_key_b :generated_key} (dao/insert-document! data/a-document op)]

        (is (= data/a-document (dissoc (dao/get-document {:id generated_key_a} op) :id)))

        (dao/del-document! {:id generated_key_a} op)

        (is (= nil (dao/get-document {:id generated_key_a} op)))

        (dao/update-document! data/b-document {:id generated_key_b} op)

        (is (= data/b-document (dissoc (dao/get-document {:id generated_key_b} op) :id)))))))

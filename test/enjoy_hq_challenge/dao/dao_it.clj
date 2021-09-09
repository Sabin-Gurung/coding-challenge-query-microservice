(ns enjoy-hq-challenge.dao.dao-it
  (:require [clojure.test :refer :all]
            [enjoy-hq-challenge.dao.dao :as dao]
            [next.jdbc :as jdbc]
            [enjoy-hq-challenge.database :as db]
            [enjoy-hq-challenge.data :as data]
            ))

(deftest create-and-fetch-user
  (jdbc/with-transaction
    [tx db/datasource {:rollback-only true}]
    (with-redefs [db/datasource tx]

      ; given no user
      (is (= nil (dao/get-user data/a-user)))

      ; when create user
      (dao/create-user! data/a-user)

      ; user should exist
      (is (= data/a-user (dao/get-user data/a-user))))
    ))

(deftest create-insert-and-delete-document
  (jdbc/with-transaction
    [tx db/datasource {:rollback-only true}]
    (with-redefs [db/datasource tx]
      (dao/create-user! data/a-user)
      (let [{generated_key_a :generated_key} (dao/insert-document! data/a-document)
            {generated_key_b :generated_key} (dao/insert-document! data/a-document)]

        (is (= data/a-document (dissoc (dao/get-document {:id generated_key_a}) :id)))

        (dao/del-document! {:id generated_key_a})

        (is (= nil (dao/get-document {:id generated_key_a})))

        (dao/update-document! data/b-document {:id generated_key_b})

        (is (= data/b-document (dissoc (dao/get-document {:id generated_key_b}) :id)))))))

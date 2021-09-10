(ns enjoy-hq-challenge.dao.dao-it
  (:require [clojure.test :refer :all]
            [enjoy-hq-challenge.dao.dao :as dao]
            [enjoy-hq-challenge.data :as data]
            [enjoy-hq-challenge.helpers :refer [rollback-test]]
            ))

(deftest create-and-fetch-user
  (rollback-test
    ; given no user
    (is (= nil (dao/get-user data/a-user)))

    ; when create user
    (dao/create-user! data/a-user)

    ; user should exist
    (is (= data/a-user (dao/get-user data/a-user)))))


(deftest create-insert-and-delete-document
  (rollback-test
    (dao/create-user! data/a-user)
    (let [{generated_key_a :generated_key} (dao/insert-document! data/a-document)
          {generated_key_b :generated_key} (dao/insert-document! data/a-document)]

      (is (= data/a-document (dissoc (dao/get-document {:id generated_key_a}) :id)))

      (dao/del-document! {:id generated_key_a})

      (is (= nil (dao/get-document {:id generated_key_a})))

      (dao/update-document! data/b-document {:id generated_key_b})

      (is (= data/b-document (dissoc (dao/get-document {:id generated_key_b}) :id))))))


(deftest query-docuemnts
  (rollback-test

    ;given
    (dao/create-user! data/a-user)
    (dao/insert-document! data/a-document)
    (dao/insert-document! data/b-document)

    (let [remove-ids (fn [col] (map #(dissoc % :id) col))]

      (is (= [data/a-document data/b-document]
             (remove-ids (dao/query-document data/a-username data/order-a-b))))

      (is (= [data/b-document data/a-document]
             (remove-ids (dao/query-document data/a-username data/order-b-a))))

      (is (= [data/a-document]
             (remove-ids
               (dao/query-document
                 data/a-username (assoc data/order-a-b :filters [data/exact-title-banana])))))

      (is (= [data/a-document data/b-document]
             (remove-ids
               (dao/query-document
                 data/a-username (assoc data/order-a-b
                                   :filters [data/substring-content-awesome])))))

      (is (= [data/a-document]
             (remove-ids
               (dao/query-document
                 data/a-username (assoc data/order-a-b
                                   :filters [data/in-a])))))

      (is (= [data/a-document data/b-document]
             (remove-ids
               (dao/query-document
                 data/a-username (assoc data/order-a-b
                                   :filters [data/in-a-b])))))

      (is (= [data/b-document data/a-document]
             (remove-ids
               (dao/query-document
                 data/a-username (assoc data/order-b-a
                                   :filters [data/in-a-b
                                             data/starts-title-ban
                                             data/substring-content-awesome])))))

      (is (= [data/b-document data/a-document]
             (remove-ids
               (dao/query-document
                 data/a-username (assoc data/order-b-a
                                   :filters [data/in-a-b
                                             data/starts-title-ban
                                             data/substring-content-awesome])))))

      (is (= [data/b-document]
             (remove-ids
               (dao/query-document
                 data/a-username (assoc data/order-b-a
                                   :filters [data/in-a-b
                                             data/substring-title-dana])))))

      (is (= []
             (remove-ids
               (dao/query-document
                 data/a-username (assoc data/order-b-a
                                   :filters [data/in-a
                                             data/substring-title-dana])))))
      )))
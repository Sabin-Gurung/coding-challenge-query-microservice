(ns enjoy-hq-challenge.user-cases.documents-should
  (:require
    [clojure.test :refer :all]
    [enjoy-hq-challenge.use-cases.documents :as SUT]
    [enjoy-hq-challenge.dao.dao :as dao]
    [mock-clj.core :refer [with-mock calls]]
    [enjoy-hq-challenge.data :as data]
    ))

(deftest saving-documents
  (let [username (:username data/a-user)]
    (testing "should insert new document if no id given"
      (with-mock [dao/insert-document! {:generated_key 1}]
                 (is (= {:status "ack" :id 1} (SUT/save username data/a-document)))
                 (is (= [[data/a-document]] (calls dao/insert-document!)))))

    (testing "should update if id is present"
      (with-mock [dao/update-document! {:update-count 1}]
                 (is (= {:status "ack" :update-count 1} (SUT/save username data/a-update-document)))
                 (is (= [[data/a-document (select-keys data/a-update-document [:id :username])]]
                        (calls dao/update-document!)))))))

(deftest fetching-documents
  (let [username (:username data/a-user)
        id (:id data/a-update-document)]
    (testing "return documents correclty"
      (with-mock [dao/get-document data/a-update-document]
                 (is (= data/a-update-document (SUT/fetch username id)))
                 (is (= [[{:username username :id id}]] (calls dao/get-document)))))

    (testing "throw when no document"
      (with-mock [dao/get-document nil]
                 (is (thrown-with-msg? Exception #"document does not exist"
                                       (SUT/fetch username id)))
                 (is (= [[{:username username :id id}]] (calls dao/get-document)))))))

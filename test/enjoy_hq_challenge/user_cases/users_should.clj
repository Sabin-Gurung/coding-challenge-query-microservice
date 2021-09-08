(ns enjoy-hq-challenge.user-cases.users-should
  (:require
    [clojure.test :refer :all]
    [enjoy-hq-challenge.use-cases.users :as SUT]
    [enjoy-hq-challenge.dao.dao :as dao]
    [mock-clj.core :refer [with-mock calls]]
    [enjoy-hq-challenge.data :as data]
    ))

(deftest create-user
  (testing "create user if user does not exist in db"
    (with-mock [dao/get-user nil
                dao/create-user! {:update-count 1}]
               (let [expected {:status "ack"}
                     actual (SUT/create-user data/a-user)]
                 (is (= actual expected))
                 (is (= [[data/a-user]] (calls dao/get-user)))
                 (is (= [[data/a-user]] (calls dao/create-user!)))
                 )))

  (testing "return ko if user already exists"
    (with-mock [dao/get-user data/a-user
                dao/create-user! nil]
               (let [expected {:status "ko"}
                     actual (SUT/create-user data/a-user)]
                 (is (= actual expected))
                 (is (= [[data/a-user]] (calls dao/get-user)))
                 (is (= [] (calls dao/create-user!)))
                 )))
  )

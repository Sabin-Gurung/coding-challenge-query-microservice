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
                 (is (= [[data/a-user]] (calls dao/create-user!))))))

  (testing "throw if already exists"
    (with-mock [dao/get-user data/a-user
                dao/create-user! nil]
               (is (thrown-with-msg? Exception #"user already exists"
                                     (SUT/create-user data/a-user)))
               (is (= [[data/a-user]] (calls dao/get-user)))
               (is (= [] (calls dao/create-user!))))))

(deftest validate-user
  (testing "return user payload without password"
    (with-mock [dao/get-user data/a-user]
               (let [expected (dissoc data/a-user :password)
                     actual (SUT/validate-user data/a-user)]
                 (is (= actual expected))
                 (is (= [[data/a-user]] (calls dao/get-user))))))

  (testing "thrown if invalid"
    (with-mock [dao/get-user nil]
               (is (thrown-with-msg? Exception #"invalid credentials"
                                     (SUT/validate-user data/a-user)))
               (is (= [[data/a-user]] (calls dao/get-user)))))
  )
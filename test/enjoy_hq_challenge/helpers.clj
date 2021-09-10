(ns enjoy-hq-challenge.helpers
  (:require [clojure.test :refer :all]
            [next.jdbc :as jdbc]
            [enjoy-hq-challenge.database :as db]))

(defmacro rollback-test
  ([& body]
   `(jdbc/with-transaction
      ~(vector 'tx `db/datasource {:rollback-only true})
      (with-redefs ~(vector `db/datasource 'tx)
        (do ~@body)))))





(comment
  (macroexpand-1
    '(rollback-test
       (print "hello")
       (print "hello")
       (print "hello")
       )
    )

  )


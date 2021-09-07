(defproject enjoy-hq-challenge "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [metosin/reitit "0.5.15"]
                 [com.github.seancorfield/next.jdbc "1.1.646"]
                 [mysql/mysql-connector-java "8.0.19"]
                 [honeysql "1.0.461"]
                 [migratus "1.3.3"]
                 [http-kit "2.5.3"]]
  :main enjoy-hq-challenge.core
  :repl-options {:init-ns enjoy-hq-challenge.core})

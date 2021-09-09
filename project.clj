(defproject enjoy-hq-challenge "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[buddy/buddy-auth "3.0.1"]
                 [clj-time "0.15.2"]
                 [com.github.seancorfield/next.jdbc "1.1.646"]
                 [honeysql "1.0.461"]
                 [http-kit "2.5.3"]
                 [metosin/reitit "0.5.15"]
                 [migratus "1.3.3"]
                 [mysql/mysql-connector-java "8.0.19"]
                 [org.clojure/clojure "1.10.1"]
                 [prismatic/schema "1.1.12"]
                 [yogthos/config "1.1.8"]]
  :main enjoy-hq-challenge.core
  :profiles {:dev {:dependencies [[mock-clj "0.2.1"]]}}
  :repl-options {:init-ns enjoy-hq-challenge.core})

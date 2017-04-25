(def +project+ 'iochibity/spec)
(def +version+ "0.1.0-SNAPSHOT")

(set-env!
;; :asset-paths #{"resources/public"}
 :resource-paths #{"src/clj"}
 ;; :source-paths #{"config"}

 :dependencies   '[[org.clojure/clojure "1.9.0-alpha15"]
                   ;; [org.clojure/clojurescript "1.9.494"]
                   [org.clojure/tools.logging "0.3.1"]
                   [org.clojure/test.check "0.9.0" :scope "test"]
                   [adzerk/boot-test "1.1.1" :scope "test"]
                   ])

(require '[boot.task.built-in]
         '[adzerk.boot-test :refer :all])

(task-options!
 repl {:port 8080}
 pom  {:project     +project+
       :version     +version+
       :description "Clojure spec lab"
       :license     {"EPL" "http://www.eclipse.org/legal/epl-v10.html"}})

(deftask systest
  "serve and repl for integration testing"
  []
  (set-env! :resource-paths #(conj % "test/system/clj"))
  (comp
   (cider)
   (repl)
   ;; (watch)
   ;; (notify :audible true)
   ;; (target)
   ))

(deftask utest
  "Unit tests"
  [n namespaces  NS  #{sym}  "test ns"]
  (set-env! :source-paths #(conj % "test/unit/clj")
            ;; :asset-paths #{"resources"}
            ;; :resource-paths #{"test/data"}
            )
  (test :namespaces namespaces
        :exclude #"data.xml.*"))


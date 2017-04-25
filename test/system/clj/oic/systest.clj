(ns oic.systest
  (:require [clojure.spec :as s]
            [clojure.spec.gen :as gen]
            [clojure.test :refer :all]
            [oic.core]
            [oic.r]
            [oic.t :as t]
            ))

(gen/generate (s/gen :oic.core/rt))
(gen/generate (s/gen :oic.core/if))

(-> (gen/generate (s/gen :oic/core)) keys)

(gen/generate (s/gen :oic.r/base-resource))

(gen/generate (s/gen :oic.r/resource))

(s/explain :oic.r/temperature {::p/temperature 72
                               :oic.scale/temperature "C"
                               :oic.core/rt [:oic.r/temperature]
                               :oic.core/if [:oic.if/baseline]
                               })

(gen/generate (s/gen :oic.r/temperature))


(gen/sample (s/gen (s/cat :k keyword? :ns (s/+ number?))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;  tests

(deftest ^:core core-rt
  (let [e (element :div {:foo "secret"})]
    (is (= e #miraj.co_dom.Element{:tag :div, :attrs {:foo "secret"}, :content ()}))))

;; (s/explain ::rt [:foo])
;; (s/explain ::rt ["foo" "bar"])
;; (s/explain ::rt ["xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"]) ;; 64 chars
;; (s/explain ::rt ["xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxy"]) ;; 65 chars


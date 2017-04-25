(ns oic.core
  (:require [clojure.spec :as s]
            [oic.if]))

;; https://github.com/OpenInterConnect/IoTDataModels/blob/master/oic.core.json
;; we use keywords, not strings, for resource types
(s/def ::rt (s/coll-of keyword? :kind vector? :min-count 1 :distinct true))
;; FIXME: name component of keyword < 65 chars


(s/def ::if (s/and (s/coll-of #{:oic.if/baseline :oic.if/ll :oic.if/b :oic.if/lb
                                :oic.if/rw :oic.if/r :oic.if/a :oic.if/s }
                              :kind vector? :distinct true)))
                   ;; (comment "The interface set supported by this resource")))

;; (s/explain ::if [:oic.if/baseline :oic.if/ll :oic.if/b])

;; json name: "n"
(s/def ::name string?) ;; read-only, "Friendly name of the resource"

;; (s/explain ::n "foo")

(s/def ::id string?) ;; read-only "Instance ID of this specific resource"

;; https://github.com/OpenInterConnect/IoTDataModels/blob/master/oic.create.json
;;(s/def ::c


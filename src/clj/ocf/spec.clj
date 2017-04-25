(ns ocf.spec
  (:require [clojure.spec :as s]))

;; from http://json-schema.org/examples.html

;; json:
;; {
;; 	"title": "Person",
;; 	"type": "object",
;; 	"properties": {
;; 		"firstName": {
;; 			"type": "string"
;; 		},
;; 		"lastName": {
;; 			"type": "string"
;; 		},
;; 		"age": {
;; 			"description": "Age in years",
;; 			"type": "integer",
;; 			"minimum": 0
;; 		}
;; 	},
;; 	"required": ["firstName", "lastName"]
;;  }

;; spec:

(s/def ::first-name string?)
(s/def ::last-name string?)
(s/def ::age (s/and integer? #(>= % 0) (comment "Age in years")))
(s/def ::person (s/keys :req [::first-name ::last-name] :opt [::age]))

(s/def :ocf.person/person (s/keys :req [::first-name ::last-name] :opt [::age]))


(s/valid? ::person {::first-name "Joe" ::last-name "Smith"})
(s/valid? ::person {::first-name "Joe" ::last-name "Smith" ::age 32})
(s/explain ::person {::first-name "Joe" ::last-name "Smith" ::age -3})
(s/explain ::person {::first-name "Joe" ::age 32})

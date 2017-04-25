(ns oic.wk
  (:require [clojure.spec :as s]
            [oic]
            [oic.prop :as p]))

;; normative definitions from OCF Core Spec 1.0.0 Draft

;; device configuration
(defmethod oic/resource-type :oic.wk/con [m]
  (let [rts (:oic.core/rt m)
        kspec (s/keys :req [:oic.core/n]
                      :opt [::loc       ; "Location information"
                            ::locn      ; "Human Friendly Name for location"
                            ::c         ; "Currency"
                            ::r         ; "Region"
                            ::ln        ; "Localized names"
                            ::dl        ; "Default langauge"
                            ])]
     (if (empty? (rest rts))
       kspec
       (s/merge
        (oic/resource-type (merge m {:oic.core/rt (rest rts)}))
        kspec))))

(s/explain :oic/resource
           {:oic.core/n "My Friendly Device Name"
            :oic.core/rt [:oic.wk/con]
            ::loc [32.777,-96.797]
            ::locn "My Location Name"
            ::c "USD"
            ::r "MyRegion"
            ::dl "en"})

(defmethod oic/resource-type :oic.wk.con/p [m]
  (let [rts (:oic.core/rt m)
        kspec (s/keys :opt [::mnpn      ; "Platform names"
                            ])]
     (if (empty? (rest rts))
       kspec
       (s/merge
        (oic/resource-type (merge m {:oic.core/rt (rest rts)}))
        kspec))))

;; example:
;; {
;; "rt": ["oic.wk.con.p"],
;; "mnpn": [ { "language": "en", "value": "My Friendly Device Name" }]
;;  }

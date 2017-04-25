(ns oic.r
  (:require [clojure.spec :as s]
            [oic]
            [oic.t :as t]))

;; https://github.com/OpenInterConnect/IoTDataModels/blob/master/oic.baseResource.json
(s/def ::base-resource (s/merge
                        :oic/core
                        (s/keys :opt [::t/precision ::t/range ::t/step ::t/value])))

(s/explain ::base-resource {::t/value 3
                            ::t/precision 2.3
                            ::t/range [1 5]
                            ::t/step 1
                            :oic.core/rt [:oic.r/foo]
                            :oic.core/if [:oic.if/baseline]
                            ::bar 9
                            })

;; https://github.com/OpenInterConnect/IoTDataModels/blob/master/oic.basecorecomposite.json
;; oic.basecorecomposite
;; (s/def ::resource (s/merge
;;                    :oic/core
;;                    ::base-resource
;;                    (s/keys :req [:oic.core/rt])))

;; (s/explain ::resource {:oic.core/rt [:foo.bar]})

;; "http://www.openconnectivity.org/ocf-apis/core/schemas/oic.collection-schema.json#",
;; from OCF Core Spec 1.0.0 Draft

(s/def ::collection (s/keys :req [::t/links] :opt [::t/id ::t/di ::t/rts ::t/drel]))
(defmethod oic/resource-type ::collection [m]
  (s/merge (s/get-spec ::collection)
           (oic/resource-type (merge m {:oic.core/rt (rest  (:oic.core/rt m))}))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; https://github.com/OpenInterConnect/IoTDataModels/blob/master/oic.r.humidity.json
(s/def ::humidity (s/keys :req [::t/humidity] :opt [::t/desired-humidity ::t/range]))
(defmethod oic/resource-type ::humidity [m]
  (let [rts (:oic.core/rt m)]
    (s/merge  (s/get-spec ::humidity)
              (oic/resource-type (merge m {:oic.core/rt (rest rts)})))))

;;   "allOf": [
;;     {"$ref": "oic.core.json#/definitions/oic.core"},
;;     {"$ref": "oic.baseResource.json#/definitions/oic.r.baseresource"},
;;     {"$ref": "#/definitions/oic.r.humidity"}
;;   ],
;;   "required": ["humidity"]
;; }

;; https://github.com/OpenInterConnect/IoTDataModels/blob/master/oic.r.sensor.json
;; generic sensor
(s/def :oic.sensor/value boolean?) ;; "true = sensed, false = not sensed."
(s/def ::sensor (s/merge (s/keys :opt [:oic.sensor/value]) ;; readOnly, "Generic sensor"
                       :oic/core
                       :oic.r/base-resource))

;; https://github.com/OpenInterConnect/IoTDataModels/blob/master/oic.r.temperature.json
(s/def :oic.scale/temperature #{"C" "F" "K"})

(s/def ::temperature (s/keys :req [::t/temperature] :opt [:oic.scale/temperature]))
(defmethod oic/resource-type ::temperature [m]
  (let [rts (:oic.core/rt m)]
      (s/merge (s/get-spec ::temperature)
               (oic/resource-type nextm (merge m {:oic.core/rt (rest rts)})))))

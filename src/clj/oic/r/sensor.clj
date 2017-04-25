(ns oic.r.sensor
  (:require [clojure.spec :as s]
            [oic]
            [oic.r]
            [oic.prop :as p]))

;; https://github.com/OpenInterConnect/IoTDataModels/blob/master/oic.r.sensor.atmosphericPressure.json
;; "Current atmospheric pressure in hPa."
(s/def ::atmospheric-pressure (s/and (s/keys :req [::p/atmospheric-pressure]) ;; readOnly
                                     ;; required: {:oic.core/rt :oic.r.sensor/atmospheric-pressure}
                                     :oic/core :oic.r.base-resource))

;; https://github.com/OpenInterConnect/IoTDataModels/blob/master/oic.r.sensor.illuminance.json
(s/def ::illuminance (s/and
                      :oic/core
                      :oic.r/base-resource
                      (s/keys :required [::p/illuminance])))

(s/explain ::illuminance {::p/illuminance 32})

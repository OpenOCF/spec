(ns oic
  (:require [clojure.spec :as s]
            [oic.core]
            [oic.t :as t]))

(defmulti resource-type (fn [x] (first (:oic.core/rt x))))

(defmethod oic/resource-type :default [m] (s/keys))
(defmethod oic/resource-type nil [m] (s/keys))

(s/def ::resource (s/multi-spec oic/resource-type :oic.core/rt))

(s/def ::core (s/keys :opt [:oic.core/rt :oic.core/if :oic.core/n :oic.core/id]))


(s/explain :oic/resource
           {::t/temperature 72
            ::t/humidity 34
            :oic.scale/temperature "C"
            :oic.core/rt [:oic.r/temperature :oic.r/humidity]
            :oic.core/if [:oic.if/baseline]
            })

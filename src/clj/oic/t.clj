(ns oic.t
  (:require [clojure.spec :as s]
            [clojure.spec.gen :as gen]))

(s/def ::string-64 (s/and string? #(< (count %) 65)))
;; (s/valid? ::string-64 "foo")

(s/def ::uri-256 (s/and #(< (count %) 257)
                    #(try (java.net.URI. %)
                          (catch java.net.URISyntaxException _ false))))
(s/explain ::uri-256 "http://example.org")
(s/explain ::uri-256 "/foo/bar")
(s/explain ::uri-256 "1#")
(s/explain ::uri-256 "1##")
(s/explain ::uri-256 "foo/bar//-/")

(s/def ::uuid #(try (java.util.UUID/fromString %)
                    (catch java.lang.IllegalArgumentException _ false)))
(let [u (str (gen/generate (gen/uuid)))]
  (s/explain ::uuid u))

;;;;;;;;;;;;;;;;  LINKS  ;;;;;;;;;;;;;;;;
;; "http://www.openconnectivity.org/ocf-apis/core/schemas/oic.oic-link-schema.json#",

;; json name: "href"
(s/def ::href ::uri-256)
(s/explain ::href "http://example.org")

;; json name:  "rel"
(s/def ::relation (s/or :ss (s/coll-of ::string-64) :s ::string-64))
;; FIXME metadata:  :default ["hosts"], "hosts"
;; "description": "The relation of the target URI referenced by the link to the context URI"

(s/explain ::relation ["foo" "bar"])
(s/explain ::relation "foo")
(s/explain ::relation "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx")
(s/explain ::relation ["xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"])
(s/explain ::relation ["xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"])
(s/explain ::relation "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx")
(s/explain ::relation 9)

;; json name: "di"
(s/def ::device-id ::uuid)
(let [u (str (gen/generate (gen/uuid)))]
  (s/explain ::device-id u))

;; json name: "bm"
(s/def ::bitmask (s/and integer? #(= 0 (bit-and 0xFC %))))
(s/explain ::bitmask 0x04)

;; json name: "p"
;; FIXME: spec says also :sec and :port for OIC
;; 1.1 "description": "Specifies the framework policies on the
;; Resource referenced by the target URI for e.g. observable and
;; discoverable",
(s/def ::policies (s/keys :req [::bitmask]))
(s/explain ::policies {::bitmask 0x04})

;; json name: "title"
(s/def ::title ::string-64)
(s/explain ::title "foo title")

;; json name: "anchor"
;; FIXME: validate string, not uri - try to construct, catch java.net.MalformedURLException
(s/def ::anchor ::uri-256)
(s/explain ::anchor "foo124")

;; json name: "ins"
;; "description": "The instance identifier for this web link in an
;; array of web links - used in collections"
;; FIXME: validate uri and uuid strings, not objects
(s/def ::instance (s/or :int integer? :uri ::uri-256 :uuid ::uuid))
(s/explain ::instance 99)
(s/explain ::instance "foo124")
(let [u (str (gen/generate (gen/uuid)))]
  (s/explain ::instance u))

;; json name: "type"
(s/def ::media-type (s/and string? #(< (count %) 65)))
(s/explain ::media-type "application/cbor")

(s/def ::media-types (s/coll-of ::media-type :kind vector? :min-count 1))
(s/explain ::media-types ["application/cbor"])
;; (s/explain ::media-types "application/cbor")
;; (s/explain ::media-types [])

;; json name: "ep"
;; "description": "URI with Transport Protocol Suites + Endpoint Locator as specified in 10.2.1"
;; FIXME: ep does not have length limit of 2566
(s/def ::ep ::uri-256)
(s/explain ::ep "http://example.org/foo")

;; json name "pri"
(s/def ::priority (s/and integer? #(> % 0)))
(s/explain ::priority 1)
;; (s/explain ::priority 0)

;; fake, wrapper
(s/def ::endpoint (s/keys :opt [::ep ::priority]))
(s/explain ::endpoint {::ep "http://example.org/foo" ::priority 1})

;; json name: "eps"
(s/def ::endpoints (s/coll-of ::endpoint :kind vector?))
(s/explain ::endpoints [{::ep "http://example.org/foo" ::priority 1}
                        {::ep "http://example.org/bar" ::priority 2}])

;; json name: none  meta name: oic.oic-link
(s/def ::link (s/keys :req [::href :oic.core/rt :oic.core/if]
                      :opt [::relation
                            ::device-id
                            ::policies
                            ::title
                            ::anchor
                            ::instance
                            ::media-types
                            ::endpoints
                            ::batch-param
                            ]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; "http://www.openconnectivity.org/ocf-apis/core/schemas/oic.collection-schema.json#",

;; (s/def ::collection (s/keys :opt [::t/id ::t/di ::t/rts ::t/drel ::t/links]))

(s/def ::id (s/or :i integer? :s string? :u ::uuid))
;; di - see device-id
(s/def ::resource-types :oic.core/rt) ;; rts
(s/def ::default-relation string?) ;; drel

;; (s/def :oic.collection/set-of-links (s/coll-of ::t/link :kind vector?))
;; (s/def :oic.collection/all-links (s/or :sl :oic.collection/set-of-links))

(s/def ::links (s/coll-of ::link :kind vector?))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; oic.r.sensor.atmosphericPressure
(s/def ::atmospheric-pressure number?) ;; scale:  hPa

;; oic.r.humidity
(s/def ::desired-humidity integer?) ;; "Desired value for Humidity"

;; oic.r.humidity
(s/def ::humidity integer?) ;; "readOnly", "Current sensed value for Humidity"

;; oic.r.sensor.illuminance
(s/def ::illuminance number?) ;; readOnly, scale: lux, "Sensed luminous flux per unit area in lux."

;; oic.r.baseresource
(s/def ::precision number?) ;; read-only, "Accuracy granularity of the exposed value"

;; oic.r.baseresource
(s/def ::range (s/and (s/cat :min (s/alt :i integer? :n number?) :max (s/alt :i integer? :n number?))
                      #(< (last (:min %)) (last (:max %)))))
(s/explain ::range [1 5])

;; oic.r.baseresource
(s/def ::step (s/or :i integer? :n number?))
;;(s/explain ::step 3.0)

(s/def ::temperature number?) ;; (comment "Current temperature setting or measurement")))

;; oic.r.baseresource
(s/def ::value (s/or :b boolean? :i integer? :n number? :m map? :s string? :v vector?))
;; (s/explain ::value [0 9])


;; oic.r.sensor.atmosphericPressure
(s/def ::atmospheric-pressure number?) ;; scale:  hPa

;; oic.r.humidity
(s/def ::desired-humidity integer?) ;; "Desired value for Humidity"

;; oic.r.humidity
(s/def ::humidity integer?) ;; "readOnly", "Current sensed value for Humidity"

;; oic.r.sensor.illuminance
(s/def ::illuminance number?) ;; readOnly, scale: lux, "Sensed luminous flux per unit area in lux."

;; oic.r.baseresource
(s/def ::precision number?) ;; read-only, "Accuracy granularity of the exposed value"

;; oic.r.baseresource
(s/def ::range (s/and (s/cat :min (s/alt :i integer? :n number?) :max (s/alt :i integer? :n number?))
                      #(< (last (:min %)) (last (:max %)))))
(s/explain ::range [1 5])

;; oic.r.baseresource
(s/def ::step (s/or :i integer? :n number?))
;;(s/explain ::step 3.0)

(s/def ::temperature number?) ;; (comment "Current temperature setting or measurement")))

;; oic.r.baseresource
(s/def ::value (s/or :b boolean? :i integer? :n number? :m map? :s string? :v vector?))
;; (s/explain ::value [0 9])

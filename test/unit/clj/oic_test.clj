;   Copyright (c) Gregg Reynolds. All rights reserved.
;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;   which can be found in the file epl-v10.html at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.

(ns ^{:doc "Unit tests for oic spec schema"
      :author "Gregg Reynolds"}
  oic-test
  (:require [clojure.spec.gen :as gen]
            ;; [clojure.java.io :as io]
            [clojure.test :refer :all]
            [clojure.spec :as s]
            [oic.r]
            [oic.t :as t]
            [oic.core]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;  tests

;; (deftest ^:core core-rt
;;   (let [e (element :div {:foo "secret"})]
;;     (is (= e #miraj.co_dom.Element{:tag :div, :attrs {:foo "secret"}, :content ()}))))

;; (s/explain ::rt [:foo])
;; (s/explain ::rt ["foo" "bar"])
;; (s/explain ::rt ["xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"]) ;; 64 chars
;; (s/explain ::rt ["xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxy"]) ;; 65 chars

(s/explain :oic/resource
           {::t/humidity 24
            ::t/range [1 5]
            ::t/temperature 72
            :oic.core/rt [:oic.r/humidity :oic.r/temperature]
            :oic.core/if [:oic.if/baseline]
            })

(s/explain :oic/resource
           {;;::t/temperature 72
            ::t/humidity 34
            :oic.scale/temperature "C"
            :oic.core/rt [:oic.r/temperature :oic.r/humidity]
            :oic.core/if [:oic.if/baseline]
            })
q
(s/explain ::t/link {::t/href "/switch/foo"
                     ::t/relation "item"
                     ::t/device-id (str (gen/generate (gen/uuid)))
                     ::t/media-type "application/cbor"
                     ::t/endpoints [{::t/ep "http://example.org/foo" ::t/priority 1}
                                    {::t/ep "http://example.org/bar" ::t/priority 2}]
                     ::t/policies {::t/bitmask 2}
                     :oic.core/rt [:oic.r/link]
                     :oic.core/if [:oic.if/baseline :oic.if/a]})

(s/explain :oic/resource
           {:oic.core/rt [:my.r/house :oic.r/collection]
            ::t/name "room"
            :oic.core/if [:oic.if/baseline]
            ::t/links [{::t/href "/foo/temp"
                        :oic.core/rt [:oic.r/temperature]
                        :oic.core/if [:oic.if/baseline]}
                       {::t/href "/switch/foo"
                        ::t/rel "item"
                        ::t/device-id (str (gen/generate (gen/uuid)))
                        ::t/media-type "application/cbor"
                        ::t/endpoints [{::t/ep "http://example.org/foo" ::t/priority 1}
                                       {::t/ep "http://example.org/bar" ::t/priority 2}]
                        ::t/policies {::t/bitmask 2}
                        :oic.core/rt [:oic.r/link :oic.r.switch.binary]
                        :oic.core/if [:oic.if/baseline :oic.if/a]}
                       ]
            })


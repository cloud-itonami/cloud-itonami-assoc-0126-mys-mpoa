(ns association.facts-test
  (:require [clojure.test :refer [deftest is]]
            [association.facts :as facts]))

(deftest mpoa-has-spec-basis
  (let [sb (facts/spec-basis "mpoa")]
    (is (= 2 (count sb)))
    (is (every? #(= "0126" (:association-rule/isic %)) sb))
    (is (every? #(= "MYS" (:association-rule/country %)) sb))))

(deftest unknown-association-has-no-spec-basis
  (is (nil? (facts/spec-basis "gapki")))
  (is (nil? (facts/spec-basis "zzz"))))

(deftest coverage-is-honest
  (let [c (facts/coverage ["mpoa" "gapki"])]
    (is (= 2 (:requested c)))
    (is (= 1 (:covered c)))
    (is (= ["gapki"] (:missing-associations c)))))

(deftest by-topic-filters
  (is (= 2 (count (facts/by-topic "mpoa" :governance))))
  (is (empty? (facts/by-topic "mpoa" :labor)))
  (is (empty? (facts/by-topic "gapki" :governance))))

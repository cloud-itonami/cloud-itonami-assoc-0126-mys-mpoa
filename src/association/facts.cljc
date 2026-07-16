(ns association.facts
  "Industry rule/history catalog for the Malaysian Palm Oil Association
  (MPOA) -- a 42nd industry-association-level source per
  ADR-2607141700 (cloud-itonami-compliance-fact-federation). The
  SECOND entry aligned to ISIC 0126 (growing of oil palm fruit),
  alongside cloud-itonami-assoc-0126-idn-gapki (Indonesia). Fills
  Malaysia's previously-open association-axis gap (noted honestly at
  tick 123) -- Malaysia now has real, individually verified facts
  across ALL THREE axes (municipality:
  cloud-itonami-municipality-mys-kuala-lumpur, tick 122; country:
  cloud-itonami-iso3166-mys statute.facts, tick 123; association:
  this entry, tick 124).

  Both entries directly WebFetch-verified against mpoa.org.my's own
  'Introduction' page, which states verbatim: 'In 1999, a
  rationalisation exercise of the various associations, advisory
  bodies, and councils of the plantation industry saw the birth of a
  single umbrella entity - the government supported Malaysian Palm
  Oil Association (MPOA).' The same page identifies the United
  Planting Association of Malaysia, one of MPOA's predecessor bodies,
  as active since 1897.

  No Wikidata Q-id was found for MPOA specifically (distinct from the
  government agency MPOB, which does have one) --
  organization.edn's :wikidata is deliberately omitted rather than
  guessed, matching the pattern used for BAP (tick 108) and FSC (tick
  121).

  An association not in `catalog` has NO spec-basis, full stop; never
  fabricate one.")

(def catalog
  "association-slug -> vector of association-rule entries."
  {"mpoa"
   [{:association-rule/id "mpoa.founding-1999-rationalisation"
     :association-rule/title "MPOA founding via rationalisation of plantation-industry bodies (Introduction)"
     :association-rule/association "mpoa"
     :association-rule/isic "0126"
     :association-rule/country "MYS"
     :association-rule/kind :governance-program
     :association-rule/url "https://www.mpoa.org.my/introduction.php"
     :association-rule/url-provenance :official-mpoa-org-my
     :association-rule/established-date "1999"
     :association-rule/retrieved-at "2026-07-17"
     :association-rule/topic #{:governance}}
    {:association-rule/id "mpoa.predecessor-united-planting-association-1897"
     :association-rule/title "United Planting Association of Malaysia, MPOA predecessor body active since 1897 (Introduction)"
     :association-rule/association "mpoa"
     :association-rule/isic "0126"
     :association-rule/country "MYS"
     :association-rule/kind :governance-program
     :association-rule/url "https://www.mpoa.org.my/introduction.php"
     :association-rule/url-provenance :official-mpoa-org-my
     :association-rule/established-date "1897"
     :association-rule/retrieved-at "2026-07-17"
     :association-rule/topic #{:governance}}]})

(defn spec-basis [association] (get catalog association))

(defn coverage
  ([] (coverage (keys catalog)))
  ([associations]
   (let [have (filter catalog associations)
         missing (remove catalog associations)]
     {:requested (count associations)
      :covered (count have)
      :covered-associations (vec (sort have))
      :missing-associations (vec (sort missing))
      :note (str "cloud-itonami-assoc-0126-mys-mpoa Wave 0 (ADR-2607141700): "
                 (count (get catalog "mpoa")) " MPOA entries seeded "
                 "with mpoa.org.my citations. "
                 "Extend `association.facts/catalog`, never fabricate an id/url.")})))

(defn by-topic [association topic]
  (filterv #(contains? (:association-rule/topic %) topic) (spec-basis association)))

# cloud-itonami-assoc-0126-mys-mpoa

Industry rule/history catalog for the **Malaysian Palm Oil
Association** (MPOA) — the SECOND entry aligned to **ISIC 0126**
(growing of oil palm fruit), alongside
[`-0126-idn-gapki`](https://github.com/cloud-itonami/cloud-itonami-assoc-0126-idn-gapki)
(Indonesia). Part of the
[`cloud-itonami`](https://github.com/cloud-itonami) compliance-fact
family (ADR-2607141700, `cloud-itonami-compliance-fact-federation`,
in `com-junkawasaki/root`).

## Sourcing note

This repo fills Malaysia's previously-open association-axis gap
(noted honestly at tick 123). Malaysia now has real, individually
verified facts across all three axes: municipality
([`cloud-itonami-municipality-mys-kuala-lumpur`](https://github.com/cloud-itonami/cloud-itonami-municipality-mys-kuala-lumpur)),
country
([`cloud-itonami-iso3166-mys`](https://github.com/cloud-itonami/cloud-itonami-iso3166-mys)),
and association (this repo).

No Wikidata Q-id was found for MPOA specifically (distinct from the
government agency MPOB, which does have one) —
`organization.edn`'s `:wikidata` is deliberately omitted rather than
guessed.

## Scope

A **read-only reference/archive** catalog — not an Advisor⊣Governor
actuation actor. It proposes or executes nothing on MPOA's behalf.

Coverage is reported honestly (see `association.facts/coverage`): an
association not in `catalog` has **no spec-basis**, full stop — never
fabricate one.

## Data

- `src/association/facts.cljc` — the catalog, source of truth.
- `schema/association-rule.edn` — DataScript schema.
- `data/datascript-tx.edn` — derived DataScript tx-data (query this
  alongside other `cloud-itonami`/`etzhayyim` compliance-fact sources via
  `com-junkawasaki/root`'s `scripts/compliance-fact-query.cljs`).

Both entries directly WebFetch-verified against `mpoa.org.my`'s own
"Introduction" page: the 1999 founding via rationalisation of
plantation-industry bodies, and the United Planting Association of
Malaysia (one of MPOA's predecessor bodies) active since 1897.

## License

AGPL-3.0-or-later (matches the `cloud-itonami-iso3166-*` /
`-municipality-*` / `-assoc-*` / `-lei-*` convention). Policy text
itself remains MPOA's; this repo stores only citation metadata
(id/title/url/dates), not full text.

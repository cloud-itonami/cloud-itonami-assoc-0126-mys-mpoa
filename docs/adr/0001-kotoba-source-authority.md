# ADR 0001: Kotoba is the MPOA catalog source authority

- Status: Accepted
- Date: 2026-07-21

## Context

The former CLJC catalog exposed unbounded host maps and sequences despite this
repository being a read-only fact source.

## Decision

`src/association_facts.kotoba` is the sole production source. Both admitted
MPOA citations retain id, title, association, ISIC, country, kind, URL, URL
provenance, established date, retrieved date, and every topic. A fixed field
vocabulary and bounded count/index ABI replace host traversal.

Unknown associations, fields, topics, negative indexes, and out-of-range
indexes return zero or typed option-none. The source declares no effects.
DataScript EDN remains a derived provider artifact.

CI executes reference semantics, restricted JavaScript, and instantiated typed
WebAssembly, and rejects production `.clj`, `.cljc`, and `.cljs` sources.

## Consequences

- Every existing citation observation remains reconstructible.
- Unknown coverage cannot create a spec-basis.
- Clojure and the JVM are compiler/test hosts only.

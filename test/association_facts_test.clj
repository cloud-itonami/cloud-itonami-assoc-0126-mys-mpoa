(ns association-facts-test
  (:require [clojure.java.io :as io]
            [clojure.java.shell :as shell]
            [clojure.test :refer [deftest is testing]]
            [kotoba.compiler.core :as compiler]
            [kotoba.compiler.ir :as ir]))

(def source (slurp "src/association_facts.kotoba"))
(defn call [kir function & args] (ir/execute kir function (vec args)))
(defn present [option] (when (second option) (nth option 2)))
(def fields ["id" "title" "association" "isic" "country" "kind" "url"
             "url-provenance" "established-date" "retrieved-at"])
(def expected
  [{"id" "mpoa.founding-1999-rationalisation"
    "title" "MPOA founding via rationalisation of plantation-industry bodies (Introduction)"
    "association" "mpoa" "isic" "0126" "country" "MYS"
    "kind" "governance-program" "url" "https://www.mpoa.org.my/introduction.php"
    "url-provenance" "official-mpoa-org-my" "established-date" "1999"
    "retrieved-at" "2026-07-17"}
   {"id" "mpoa.predecessor-united-planting-association-1897"
    "title" "United Planting Association of Malaysia, MPOA predecessor body active since 1897 (Introduction)"
    "association" "mpoa" "isic" "0126" "country" "MYS"
    "kind" "governance-program" "url" "https://www.mpoa.org.my/introduction.php"
    "url-provenance" "official-mpoa-org-my" "established-date" "1897"
    "retrieved-at" "2026-07-17"}])

(deftest reference-preserves-the-complete-catalog
  (let [kir (:kir (compiler/compile-source source :js-kotoba-v1))
        observed (mapv (fn [index]
                         (into {} (map (fn [field]
                                         [field (present (call kir 'entry-field "mpoa" index field))])
                                       fields)))
                       (range (call kir 'entry-count "mpoa")))]
    (is (= expected observed))
    (is (= 1 (call kir 'association-covered? "mpoa")))
    (is (zero? (call kir 'association-covered? "gapki")))
    (is (= [1 1] (mapv #(call kir 'topic-count "mpoa" %) [0 1])))
    (is (= ["governance" "governance"]
           (mapv #(present (call kir 'topic "mpoa" % 0)) [0 1])))
    (is (= 2 (call kir 'by-topic-count "mpoa" "governance")))
    (is (= (mapv #(get % "id") expected)
           (mapv #(present (call kir 'by-topic-id "mpoa" "governance" %)) [0 1])))
    (is (= #{} (set (:effects kir))))
    (testing "unknown association, field, topic, and indexes fail closed"
      (is (zero? (call kir 'entry-count "gapki")))
      (is (nil? (present (call kir 'entry-field "gapki" 0 "id"))))
      (is (nil? (present (call kir 'entry-field "mpoa" -1 "id"))))
      (is (nil? (present (call kir 'entry-field "mpoa" 2 "id"))))
      (is (nil? (present (call kir 'entry-field "mpoa" 0 "unknown"))))
      (is (nil? (present (call kir 'topic "mpoa" 0 1))))
      (is (zero? (call kir 'by-topic-count "mpoa" "labor")))
      (is (nil? (present (call kir 'by-topic-id "mpoa" "labor" 0)))))))

(defn compiler-root []
  (nth (iterate #(.getParent ^java.nio.file.Path %)
                (java.nio.file.Path/of (.toURI (io/resource "kotoba/compiler/core.clj")))) 4))
(defn base64 [value] (.encodeToString (java.util.Base64/getEncoder) value))

(deftest restricted-javascript-and-typed-wasm-conform-semantically
  (let [javascript (compiler/compile-source source :js-kotoba-v1)
        wasm (compiler/compile-source source :wasm32-browser-kotoba-v1)
        js64 (base64 (.getBytes ^String (:source javascript) "UTF-8"))
        wasm64 (base64 ^bytes (:bytes wasm))
        probe (shell/sh
                "node" "--input-type=module" "-e"
                (str "import(process.argv[1]).then(async host=>{"
                     "const j=await import('data:text/javascript;base64," js64 "');"
                     "const w=await host.instantiateKotoba(Buffer.from(process.argv[2],'base64'));"
                     "const run=x=>{if(x['entry-count']('mpoa')!==2n||x['entry-count']('gapki')!==0n)throw Error('count');"
                     "if(x['entry-field']('mpoa',0n,'id')[2]!=='mpoa.founding-1999-rationalisation')throw Error('first');"
                     "if(x['entry-field']('mpoa',1n,'established-date')[2]!=='1897')throw Error('second');"
                     "if(x['topic']('mpoa',1n,0n)[2]!=='governance'||x['by-topic-count']('mpoa','governance')!==2n)throw Error('topic');"
                     "if(x['entry-field']('mpoa',2n,'id')[1]!==false||x['entry-field']('mpoa',0n,'unknown')[1]!==false)throw Error('reject');};"
                     "run(j.instantiateKotoba({}));run(w.instance.exports);"
                     "}).catch(e=>{console.error(e);process.exit(99)})")
                (.toString (.toUri (.resolve (compiler-root) "runtime/browser-host.mjs"))) wasm64)]
    (is (zero? (:exit probe)) (str (:out probe) (:err probe)))))

(deftest production-source-authority
  (is (= ["src/association_facts.kotoba"]
         (->> (file-seq (io/file "src")) (filter #(.isFile %)) (map str) sort vec))))

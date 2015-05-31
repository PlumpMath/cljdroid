(defproject org.kipz/cljdroid "0.0.1-SNAPSHOT"
  :description "Clojure/Core Async/Android stub"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}

  :global-vars {*warn-on-reflection* true}

  :source-paths ["src/clojure" "src"]
  :java-source-paths ["src/java"]
  :javac-options ["-target" "1.7" "-source" "1.7" "-Xlint:-options"]

  :plugins [[lein-droid "0.4.0-20150405.134120-5" :exclusions [org.clojure/clojure]]]

  :dependencies [[org.clojure-android/clojure "1.7.0-alpha6" :use-resources true]
                 [neko/neko "3.2.0"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha" :exclusions [org.clojure/clojure]]]

  :profiles {:default [:dev]

             :dev
                      [:android-common
                       {:dependencies [[org.clojure/tools.nrepl "0.2.10" :exclusions [org.clojure/clojure]]]
                        :target-path  "target/debug"
                        :android      {:aot                     :all-with-unused
                                       ;; The namespace of the app package - having a
                                       ;; different one for dev and release allows you to
                                       ;; install both at the same time.
                                       :rename-manifest-package "org.kipz.cljdroid.debug"
                                       :manifest-options        {:app-name "Android sample debug"}
                                       }}]

             :release
                      [:android-common :android-user
                       {:target-path "target/release"
                        :android     {
                                      :ignore-log-priority [:debug :verbose]
                                      :aot                 :all
                                      :build-type          :release}}]

             :lean
                      [:release
                       {:dependencies ^:replace [[org.skummet/clojure "1.7.0-alpha5-r4" :use-resources true]
                                                 [neko/neko "3.2.0"]
                                                 [org.clojure/core.async "0.1.346.0-17112a-alpha" :exclusions [org.clojure/clojure]]]
                        :exclusions   [[org.clojure/clojure]
                                       [org.clojure-android/clojure]]
                        :jvm-opts     ["-Dclojure.compile.ignore-lean-classes=true"]
                        :global-vars  ^:replace {clojure.core/*warn-on-reflection* true}
                        :android      {
                                       :proguard-execute   true
                                       :proguard-conf-path "proguard.conf"
                                       :lean-compile       true
                                       :skummet-skip-vars  ["#'neko.init/init"
                                                            "#'neko.context/context"
                                                            "#'neko.resource/package-name"
                                                            "#'neko.-utils/keyword->static-field"
                                                            "#'neko.-utils/keyword->setter"
                                                            "#'neko.ui.traits/get-display-metrics"
                                                            "#'org.kipz.cljdroid.main/MainActivity-onCreate"
                                                            "#'org.kipz.cljdroid.main/MainActivity-init"]}}]}

  :android {
            :dex-opts         ["-JXmx4096M"]
            ;; Target version affects api used for compilation.
            :target-version   19

            ;; Sequence of namespaces that should not be compiled.
            :aot-exclude-ns   ["clojure.parallel"
                               "clojure.core.reducers"
                               "cljs.core.async.macros"
                               "cljs.core.async.impl.ioc-macros"]

            ;; This specifies replacements which are inserted into
            ;; AndroidManifest-template.xml at build time. See Clostache for
            ;; more advanced substitution syntax. Version name and code are
            ;; automatically inserted
            :manifest-options {:app-name "@string/app_name"}})

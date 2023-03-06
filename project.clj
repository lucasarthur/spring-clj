(defproject spring-clj "1.0.0"
  :description "use spring boot with clojure!"
  :url "https://github.com/lucasarthur/spring-clj"
  :license {:name "GNU General Public License v3.0"
            :url "https://www.gnu.org/licenses/gpl-3.0.pt-br.html"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [reactor-clj "1.0.0"]
                 [org.springframework.boot/spring-boot-starter-webflux "3.0.2"]
                 [org.springframework.boot/spring-boot-starter-actuator "3.0.2"]
                 [cheshire "5.11.0"]
                 [org.clojure/tools.logging "1.2.4"]]
  :source-paths ["src" "srcj"]
  :java-source-paths ["srcj"]
  :aot :all)

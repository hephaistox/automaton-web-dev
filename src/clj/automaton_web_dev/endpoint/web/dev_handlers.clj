(ns automaton-web-dev.endpoint.web.dev-handlers
  (:require
   [automaton-core.adapters.deps-edn :as deps-edn]
   ;[ring.middleware.reload           :as mr]
  ))

(def runnables
  "Listof classpath that should trigger a reload"
  (-> (deps-edn/load-deps)
      (deps-edn/extract-paths)))

(defn wrap-nocache
  "Dev wrapper to prevent caching of all requests, helpfull to run multiple app locally on the same port for instance"
  [handler]
  (fn [request]
    (-> request
        handler
        (assoc-in [:headers "Pragma"] "no-cache"))))

(defn wrap-reload
  "Reload clj as they are saved"
  [handler]
  handler
  ;;FIX Deactivated unless [US970](https://www.notion.so/hephaistox/Ring-reload-middleware-PR-about-as-alias-359d8789694a4bb8b9843646ec8126f1?pvs=4) is fixed
  #_(-> handler
        (mr/wrap-reload {:dirs runnables})))

(def middlewares
  "Middlewares specific for development environment"
  [wrap-reload wrap-nocache])

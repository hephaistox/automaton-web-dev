(ns automaton-web-dev.endpoint.web.dev-handlers
  (:require [ring.middleware.reload :as mr]
            [automaton-core.adapters.deps-edn :as deps-edn]))

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
  (-> handler
      (mr/wrap-reload {:dirs runnables})))

(def middlewares "Middlewares specific for development environment" [wrap-reload wrap-nocache])

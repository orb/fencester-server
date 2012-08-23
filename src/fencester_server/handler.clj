(ns fencester-server.handler
  (:use compojure.core, hiccup.core, cheshire.core)
  (:require [compojure.handler :as handler]
            [clj-http.client :as client]
            [clojure.string :only (join split)]
            [compojure.route :as route]))


(defn post-to-urbanairship [alias-list location msg]
  (client/post "https://go.urbanairship.com/api/push/" {
                                                        :basic-auth ["dL3-GcxfRWCROH9Frwj7Gg" "5U2NeTYtTb2FEDVArqt7Ow"]
                                                        :form-params {
                                                                      :aliases alias-list
                                                                      :aps {:alert (str "location : " location "|" msg)}}
                                                        :content-type :json}))
(defroutes app-routes
  (GET "/post-to-urbanairship" {params :params}
       (def alias-list (clojure.string/split (params :alias) #"\|"))
       (post-to-urbanairship alias-list (params :location) (params :message)))
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))

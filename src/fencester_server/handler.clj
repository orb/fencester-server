(ns fencester-server.handler
  (:use compojure.core, hiccup.core, cheshire.core )
  (:use [monger.core :only [connect! connect set-db! get-db]]
        [monger.collection :only [insert insert-batch]])
  (:import [com.mongodb MongoOptions ServerAddress]
           [org.bson.types ObjectId]
           [com.mongodb DB WriteConcern])
  (:require [compojure.handler :as handler]
            [clj-http.client :as client]
            [clojure.string :only (join split)]
            [monger.core :as mg]
            [compojure.route :as route]))

(connect!)
(set-db! (monger.core/get-db "fencester"))

(defn post-to-urbanairship [alias-list location msg]
  (insert "location" {:location location
                      :msg msg
                      :alias alias-list
                      })
  (client/post "https://go.urbanairship.com/api/push/" {
                                                        :basic-auth ["dL3-GcxfRWCROH9Frwj7Gg" "5U2NeTYtTb2FEDVArqt7Ow"]
                                                        :form-params {
                                                                      :aliases alias-list
                                                                      :aps {:alert (str "location:" location "|" msg)}}
                                                        :content-type :json}))
(defroutes app-routes
  (GET "/post-to-urbanairship" {params :params}
       (def alias-list (clojure.string/split (params :alias) #"\|"))
       (post-to-urbanairship alias-list (params :location) (params :msg)))
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))

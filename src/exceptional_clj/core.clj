(ns exceptional-clj.core
  (:require [clj-http.client :as client]
            [clojure.data.json :as json]
            [gears.date-time :as date-time]
            [gears.exceptions :as exceptions]
            [clojure.java.io :as io])
  (:import (java.util.zip GZIPOutputStream)
           (java.io InputStream
                    File
                    ByteArrayInputStream
                    ByteArrayOutputStream)))

(defn gzipped-response [resp]
  (let [body (resp :body)
        bout (ByteArrayOutputStream.)
        out (GZIPOutputStream. bout)
        resp (assoc-in resp [:headers "content-encoding"] "gzip")]
    (io/copy (resp :body) out)
    (.close out)
    (if (instance? InputStream body)
      (.close body))
    (assoc resp :body (ByteArrayInputStream. (.toByteArray bout)))))

(defn- body-to-bytes [resp]
  (let [body-stream (:body resp)
        bytes-available (.available body-stream)
        bytes (byte-array bytes-available)]
    (do
      (.read body-stream bytes 0 bytes-available)
      (assoc resp :body bytes))))

(defn- to-json [e]
  (let [occurred-at (date-time/date-to-string (date-time/todays-date))
        message (.getMessage e)
        class (.toString (.getClass e))
        stack-trace (exceptions/stack-trace-as-vec e)
        body {"application_environment" {"application_root_directory" ""
                                         "env" {}}
              "exception" {"occurred_at" occurred-at
                           "message" message
                           "backtrace" stack-trace
                           "exception_class" class}
              "client" {"name" "exceptional-clj"
                        "version" "0.0.1"
                        "protocol_version" 6}}]
    (json/json-str body)))

(defn handler [exceptional-api-key e]
  (let [req (body-to-bytes (gzipped-response
                            {:body (to-json e)
                             :headers {"Content-Type" "application/json"
                                       "Accept" "application/json"
                                       "User-Agent" "exceptional-clj/1.0.0-SNAPSHOT"}}))]
        (client/post
         (str "http://api.getexceptional.com/api/errors?api_key="
              exceptional-api-key
              "&protocol_version=6")
         req)))

(ns com.indoles.clj.state-chan-test
  (:require [clojure.test :refer :all]
            [clojure.core.async :as async]
            [com.indoles.clj.state-chan :refer :all]))

(deftest a-test
  (testing "Simple"
    (let [c (init 1)]
      (is (= 2 (respond-to c inc))))))

(deftest b-test
  (testing "Unmodified"
    (let [c (init 1)]
      (respond-to c (async/chan) inc false)
      (is (= 1 ) (state c)))))

#!/usr/bash
mvn clean
kubectl delete -f  QRCode.yml
kubectl delete -n tool configmap QRCode-filebeat-config
kubectl get pod -n tool -o wide
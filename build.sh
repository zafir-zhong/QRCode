#!/usr/bash
mvn clean package
docker build -t tool/QRCode:1.0.00-release .
kubectl create -n tool configmap QRCode-filebeat-config --from-file=config
kubectl apply -f QRCode.yml
kubectl get pod -n tool -o wide
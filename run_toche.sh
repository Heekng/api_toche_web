#!/bin/bash

EXIST_A=$(sudo docker-compose -p toche-a -f docker-compose.a.yml ps | grep Up)

if [ -z "${EXIST_A}" ]
then
  START_CONTAINER=a
  TERMINATE_CONTAINER=b
  START_PORT=9005
  TERMINATE_PORT=9006
else
  START_CONTAINER=b
  TERMINATE_CONTAINER=a
  START_PORT=9006
  TERMINATE_PORT=9005
fi

echo "toche-${START_CONTAINER} up"

sudo docker-compose -p toche-${START_CONTAINER} -f docker-compose.${START_CONTAINER}.yml up -d --build

for cnt in {1..10}
do
  echo "check server start"
  UP=$(curl -s http://127.0.0.1:${START_PORT}/serverStatus/health | grep 'UP')
  if [ -z "${UP}" ]
  then
    echo "server not start.."
    if [ $cnt -eq 10 ]
    then
      echo "deployment failed"
      exit 1
    fi
  else
    break
  fi

  echo "wait 10 seconds"
  sleep 10
done

echo "server start"

echo "change enginx server port"

sudo sed -i "s/${TERMINATE_PORT}/${START_PORT}/" /etc/nginx/conf.d/toche-uri.inc

echo "nginx reload"
sudo service nginx reload

echo "toche-${TERMINATE_CONTAINER} down"
sudo docker-compose -p toche-${TERMINATE_CONTAINER} -f docker-compose.${TERMINATE_CONTAINER}.yml down

echo "deploy end"

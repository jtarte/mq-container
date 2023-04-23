

docker run \
  --env LICENSE=accept \
  --env MQ_QMGR_NAME=QM2 \
  --publish 1414:1414 \
  --publish 9443:9443 \
  --publish 9157:9157 \
  --detach \
  --rm \
  -v $(pwd)/qm2.mqsc:/etc/mqm/qm2.mqsc \
  --name myqm2 \
  ibmcom/mq

  docker run \
  --env LICENSE=accept \
  --env MQ_QMGR_NAME=QM1 \
  --publish 1415:1414 \
  --publish 9444:9443 \
  --publish 9158:9157 \
  --detach \
  --rm \
  -v $(pwd)/qm1.mqsc:/etc/mqm/qm1.mqsc \
  --name myqm1 \
  ibmcom/mq


pour ecouter sur QM2
  export MQSERVER='APPCHL/TCP/localhost(1414)'


/opt/mqm/samp/bin/amqsgetc Q1 QM2    



pour envoyer message ur QM1
  export MQSERVER='APPCHL/TCP/localhost(1415)'

echo 'hello' | /opt/mqm/samp/bin/amqsputc Q1 QM1


Attention au droits QM2, pour demo donner droit admin user app sur QM2
Attention a la resolution de noms sur le channel sender. utiliser l'IP peut e^tre mieux avec docker localement

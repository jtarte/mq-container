# Simple Access to MQ (in Container) demo

## Define MQ configuration

Review the qm1.mqsc file

sample content: 
```
* Application Queue
DEFINE QLOCAL('APPQ') REPLACE

* Application channels (Application)
DEFINE CHANNEL('APPCHL') CHLTYPE(SVRCONN) TRPTYPE(TCP) MCAUSER('app') REPLACE

* Application channel authentication rules
SET CHLAUTH('*') TYPE(ADDRESSMAP) ADDRESS('*') USERSRC(NOACCESS) DESCR('Back-stop rule - Blocks everyone') ACTION(REPLACE)
SET CHLAUTH('APPCHL') TYPE(ADDRESSMAP) ADDRESS('*') USERSRC(CHANNEL) CHCKCLNT(ASQMGR) DESCR('Allows connection via APP channel') ACTION(REPLACE)

* Developer authority records
SET AUTHREC PROFILE('APPQ') PRINCIPAL('app') OBJTYPE(QUEUE) AUTHADD(BROWSE,GET,INQ,PUT)

* Developer connection authentication
DEFINE AUTHINFO('DEV.AUTHINFO') AUTHTYPE(IDPWOS) CHCKCLNT(REQDADM) CHCKLOCL(OPTIONAL) ADOPTCTX(YES) REPLACE
ALTER QMGR CONNAUTH('DEV.AUTHINFO')
REFRESH SECURITY(*) TYPE(CONNAUTH)
```

## Launch the MQ container 

launch the docker container:
```
docker run \
  --env LICENSE=accept \
  --env MQ_QMGR_NAME=QM1 \
  --publish 1414:1414 \
  --publish 9443:9443 \
  --publish 9157:9157 \
  --detach \
  --rm \
  -v $(pwd)/qm1.mqsc:/etc/mqm/qm1.mqsc \
  --name myqm \
  ibmcom/mq
``` 

## Test with native client

Define the MQ connection env variable
```
export MQSERVER='APPCHL/TCP/localhost(1414)'
```

Put some messages in the queue
```
/opt/mqm/samp/bin/amqsputc APPQ QM1
```

Get messages from the queue
```
/opt/mqm/samp/bin/amqsgetc APPQ QM1
```

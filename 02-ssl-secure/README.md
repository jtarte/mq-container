# Secure Access to MQ (in Container) demo

The demo is using TLS communication

## Prepare key and certificate

Create the certificate and the key
``` 
openssl req -newkey rsa:2048 -nodes -keyout qm1.key -subj "/CN=QM1" -x509 -days 3650 -out qm1.crt
```

### For native client

Prepare a certificate database
```
 runmqakm -keydb -create -db qmkey.kdb -pw password -type cms -stash
```

Add the certificate to the datbase
```
runmqakm -cert -add -db qmkey.kdb -label qm1 -file qm1.crt -format ascii -stashed
```

Optional: check the content of the database
```
runmqakm -cert -list -db qmkey.kdb -stashed
runmqakm -cert -details -db qmkey.kdb -stashed -label qm1
```

### For java client

Prepare a truststore and import the certificate
```
keytool -keystore clientkey.jks -storetype jks -importcert -file key.crt -alias server-certificate
```

### Move the certificate & key

Move the certifciate and key file to the directory that will be used by the docker
```
mv qm1.* ./cert
```

## Define MQ configuration

Review the qm1.mqsc file

sample content: 
```
* Application Queue
DEFINE QLOCAL('APPQ') REPLACE

* Application channels (Application)
DEFINE CHANNEL('APPCHL') CHLTYPE(SVRCONN) TRPTYPE(TCP) SSLCAUTH(OPTIONAL) SSLCIPH('ANY_TLS12_OR_HIGHER') MCAUSER('app') REPLACE

*SET CHLAUTH('APPCHL') TYPE(BLOCKUSER) USERLIST('nobody') ACTION(ADD)
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

To launch the docker container, run:
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
  -v $(pwd/cert:/etc/mqm/pki/keys/qm1)
  --name myqm \
  ibmcom/mq
``` 

## Test with native client

Define the MQ connection env variable
```
export MQSERVER='APPCHL/TCP/localhost(1414)'
export MQSSLKEYR=$(pwd)/qmkey
```

Put some messages in the queue
```
/opt/mqm/samp/bin/amqsputc APPQ QM1
```

Get messages from the queue
```
/opt/mqm/samp/bin/amqsgetc APPQ QM1
```

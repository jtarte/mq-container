# Execute Pyhton sample MQ applications

This sample application was written with pyhton3.

The Queue Manager is configured as described [here](../README.MD)

## Load MQ library.

The sample application uses the Python MQ library, `pymqi`. 

This Python library needs to load MQ libraries. To allow that, you should define the environement varaible `DYLD_LIBRARY_PATH`:
```
export export DYLD_LIBRARY_PATH=/opt/mqm/lib64:/opt/mqm/lib
```

## Set your environment

Define your context by setting the value in `config.json` file.

Sample values of `config.json`
```
{
    "queue_manager": "QM1",
    "channel": "APPCHL",
    "host" : "localhost",
    "port" : "1414",
    "queue": "APPQ",
}
``` 

## Run the application

Send 10 message into the target queue

```
python3 ./python/mq_put.py ./config.json
```

Read the messages present in the target queue:
```
python3 ./python/mq_put.py ./config.json
```



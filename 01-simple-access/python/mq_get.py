from pickle import NONE
import pymqi
import json, sys

argv = sys.argv[1:]

f = open(argv[0])

mqconn_options = json.load(f)


conn_info = "%s(%s)" % (mqconn_options["host"], mqconn_options["port"])

qmgr = pymqi.QueueManager(None)
qmgr.connectTCPClient(mqconn_options["queue_manager"], pymqi.cd(), mqconn_options["channel"], conn_info,"","")

getq = pymqi.Queue(qmgr, mqconn_options["queue"])

while(True):
    try:
        msg = getq.get()
        print(msg)
    except pymqi.MQMIError as e:
        break
getq.close()
qmgr.disconnect()

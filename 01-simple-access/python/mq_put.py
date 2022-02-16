import pymqi
import json, sys

argv = sys.argv[1:]

f = open(argv[0])

mqconn_options = json.load(f)

print(mqconn_options)

conn_info = "%s(%s)" % (mqconn_options["host"], mqconn_options["port"])

qmgr = pymqi.QueueManager(None)
qmgr.connectTCPClient(mqconn_options["queue_manager"], pymqi.cd(), mqconn_options["channel"], conn_info,"","")

putq = pymqi.Queue(qmgr, mqconn_options["queue"])

for i in range(10):
    putq.put("hello Python " + str(i))

putq.close()
qmgr.disconnect()

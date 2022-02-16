from pickle import NONE
import pymqi
import json, sys

argv = sys.argv[1:]

f = open(argv[0])

mqconn_options = json.load(f)

conn_info = "%s(%s)" % (mqconn_options["host"], mqconn_options["port"])


cd = pymqi.CD()
cd.ChannelName = bytes(mqconn_options["channel"],'utf-8')
cd.ConnectionName = bytes(conn_info,'utf-8')
cd.ChannelType = pymqi.CMQC.MQCHT_CLNTCONN
cd.TransportType = pymqi.CMQC.MQXPT_TCP
cd.SSLCipherSpec = b'ANY_TLS12'

sco = pymqi.SCO()
#include file name but not file extension
sco.KeyRepository = bytes(mqconn_options["key_rep"],'utf-8')
sco.CertificateLabel =bytes(mqconn_options["key_label"],'utf-8')

qmgr = pymqi.QueueManager(None)
#qmgr.connectTCPClient(mqconn_options["queue_manager"], pymqi.cd(), mqconn_options["channel"], conn_info,"","")
qmgr.connect_with_options(mqconn_options["queue_manager"], cd=cd, sco=sco)

getq = pymqi.Queue(qmgr, mqconn_options["queue"])

while(True):
    try:
        msg = getq.get()
        print(msg)
    except pymqi.MQMIError as e:
        break
getq.close()
qmgr.disconnect()

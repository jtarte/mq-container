package com.ibm.el.swat.mq;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQException;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;

import com.ibm.mq.constants.CMQC;
import com.ibm.mq.constants.MQConstants;


public class MQSample {

    protected String host;
    protected int port;
    protected String qmgr;
    protected String channel;
    protected String queue;
    protected MQQueueManager destQMgr;
    protected MQQueue destQueue;

    

    protected void loadConfig(String filename) throws FileNotFoundException
    {
        InputStream is = new FileInputStream(new File(filename));
        

        JSONTokener tokener = new JSONTokener(is);
        JSONObject object = new JSONObject(tokener);

        host = object.getString("host");
        port = object.getInt("port");
        qmgr = object.getString("queue_manager");
        channel = object.getString("channel");
        queue = object.getString("queue");
    }

    protected void initConnection() throws MQException
    {
        int openOptions = CMQC.MQOO_INQUIRE | CMQC.MQOO_INPUT_AS_Q_DEF | MQConstants.MQOO_OUTPUT;
        MQEnvironment.hostname = host;
        MQEnvironment.port = port;
        MQEnvironment.channel = channel;

        destQMgr = new MQQueueManager("QM1");
        destQueue = destQMgr.accessQueue("APPQ", openOptions);

    }

    protected MQQueue getDestQueue(){
        return destQueue;
    }

    protected void closeConnection() throws MQException
    {
        destQueue.close();
        destQMgr.disconnect();
    }
}

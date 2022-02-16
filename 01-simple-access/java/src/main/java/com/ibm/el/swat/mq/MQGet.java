package com.ibm.el.swat.mq;

import java.io.IOException;

import com.ibm.mq.MQException;
import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQQueue;

public class MQGet{

    private static void readMessage(MQQueue destQueue, MQGetMessageOptions gmo) throws IOException
    {
        boolean more = true;
        while(more){
            try {
                MQMessage msg = new MQMessage();
                destQueue.get(msg,gmo);

                String msgStr = msg.readStringOfByteLength(msg.getMessageLength());
                System.out.println("message: "+msgStr+" was received");   
            }
            catch(MQException e)
            {
                more = false;
            }  
        }
    }

    public static void main(String[] args)
    {
        try {
            MQPut instance =  new MQPut();

            instance.loadConfig("/Users/jtarte/dev/mq-container/01-simple-access/config.json");
            instance.initConnection();

            MQQueue destQueue = instance.getDestQueue();
            
            MQGetMessageOptions gmo = new MQGetMessageOptions();
            readMessage(destQueue, gmo);
            
           instance.closeConnection();
                   
        } catch (Exception e) {
            System.out.println("Exception: " + e);
            e.printStackTrace();
        }

    }
}
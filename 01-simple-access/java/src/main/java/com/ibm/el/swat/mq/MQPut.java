package com.ibm.el.swat.mq;

import com.ibm.mq.MQMessage;
import com.ibm.mq.MQPutMessageOptions;
import com.ibm.mq.MQQueue;

public class MQPut extends MQSample{



    public static void main(String[] args)
    {
        try 
        {
            MQPut instance =  new MQPut();

            instance.loadConfig("/Users/jtarte/dev/mq-container/01-simple-access/config.json");
            instance.initConnection();

            MQQueue destQueue = instance.getDestQueue();
            for(int i=0; i<10;i++){
                MQMessage msg = new MQMessage();
                String m = "Blah...blah...bleah...test message no."+i+"...!";
                msg.writeUTF(m);
                MQPutMessageOptions pmo = new MQPutMessageOptions();
                destQueue.put(msg, pmo);
                System.out.println("message: "+m+" was sent");
            }
            instance.closeConnection();
            System.out.println("------------------------success...");            
        } catch (Exception e) {
            System.out.println("Exception: " + e);
            e.printStackTrace();
        }

    }





}
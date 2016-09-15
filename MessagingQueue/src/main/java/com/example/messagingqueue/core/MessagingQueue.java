package com.example.messagingqueue.core;

import com.example.messagingqueue.datasource.DataSource;
import com.example.messagingqueue.model.request.CreateMessageRequest;
import com.example.messagingqueue.model.response.CreateMessageResponse;
import com.example.messagingqueue.model.response.MessageResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Chetan
 */
@Slf4j
public class MessagingQueue {

    private static int queueSize = 2048;
    public static MessagingQueue messageQueueInstance;

    private BlockingQueue<CreateMessageRequest> messageRequestBlockingQueue;
    private Map<String, CreateMessageRequest> messageMap;

    public static MessagingQueue getMessageQueueInstance() {
        if (messageQueueInstance == null) {
            synchronized (MessagingQueue.class) {
                if (messageQueueInstance == null) {
                    messageQueueInstance = new MessagingQueue(queueSize);
                    return messageQueueInstance;
                } else return messageQueueInstance;
            }
        }
        return messageQueueInstance;
    }

    private MessagingQueue(int queueSize) {
        this.queueSize = queueSize;
        messageMap = new HashMap<>();
        messageRequestBlockingQueue = new LinkedBlockingQueue<>(queueSize);
    }

    public synchronized CreateMessageResponse sendMessage(CreateMessageRequest messageRequest, DataSource dataSource) {
        try {
            messageRequestBlockingQueue.put(messageRequest);
            messageMap.put(messageRequest.getMessageId(), messageRequest);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return dataSource.sendMessage(messageRequest);
    }

    public synchronized List<MessageResponse> receiveMessage(DataSource dataSource) {
        List<MessageResponse> messageResponseList = dataSource.receiveMessage();
        for (MessageResponse messageResponse : messageResponseList) {
            if (messageMap.containsKey(messageResponse.getMessageId())) {
                messageRequestBlockingQueue.remove(messageMap.get(messageResponse.getMessageId()));
                messageMap.remove(messageResponse.getMessageId());
            }
        }
        return messageResponseList;
    }

    public synchronized void notifyAndUpdateProcessedMessages(List<CreateMessageRequest> messageRequestList, DataSource dataSource) {
        dataSource.notifyAndUpdateProcessedMessages(messageRequestList);
    }
}

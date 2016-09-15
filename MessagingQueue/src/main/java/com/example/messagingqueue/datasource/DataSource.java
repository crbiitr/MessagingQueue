package com.example.messagingqueue.datasource;

import com.example.messagingqueue.model.request.CreateMessageRequest;
import com.example.messagingqueue.model.response.CreateMessageResponse;
import com.example.messagingqueue.model.response.MessageResponse;

import java.util.List;

/**
 * Created by Chetan
 */
public interface DataSource {
    CreateMessageResponse sendMessage(CreateMessageRequest messageRequest);

    List<MessageResponse> receiveMessage();

    void notifyAndUpdateProcessedMessages(List<CreateMessageRequest> messageRequestList);
}

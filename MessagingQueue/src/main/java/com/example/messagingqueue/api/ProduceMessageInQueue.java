package com.example.messagingqueue.api;

import com.example.messagingqueue.core.MessagingQueue;
import com.example.messagingqueue.datasource.DataSource;
import com.example.messagingqueue.datasource.DataSourceFactory;
import com.example.messagingqueue.model.request.CreateMessageRequest;
import com.example.messagingqueue.model.response.CreateMessageResponse;
import org.skife.jdbi.v2.DBI;

import javax.ws.rs.core.Response;
import java.util.UUID;

/**
 * Created by Chetan
 */
public class ProduceMessageInQueue {
    private DBI jdbi;
    private DataSource dataSource;

    public ProduceMessageInQueue(DBI jdbi) {
        this.jdbi = jdbi;
        dataSource = DataSourceFactory.getDataSource("MySQL", jdbi);
    }

    public Response addMessageInQueue() {
        String id = UUID.randomUUID().toString();
        CreateMessageRequest createMessageRequest = new CreateMessageRequest();
        createMessageRequest.setMessageId(id);
        MessagingQueue messagingQueue = MessagingQueue.getMessageQueueInstance();
        CreateMessageResponse createdMessageResponse = messagingQueue.sendMessage(createMessageRequest, dataSource);
        return Response.status(200).entity(createdMessageResponse).build();
    }
}

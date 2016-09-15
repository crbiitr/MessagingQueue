package com.example.messagingqueue.api;

import com.example.messagingqueue.core.MessagingQueue;
import com.example.messagingqueue.datasource.DataSource;
import com.example.messagingqueue.datasource.DataSourceFactory;
import com.example.messagingqueue.model.response.MessageResponse;
import org.skife.jdbi.v2.DBI;

import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by Chetan
 */
public class ConsumeMessageFromQueue {
    private DBI jdbi;
    private DataSource dataSource;

    public ConsumeMessageFromQueue(DBI jdbi) {
        this.jdbi = jdbi;
        dataSource = DataSourceFactory.getDataSource("MySQL", jdbi);
    }

    public Response consumeMessagesFromQueue() {
        MessagingQueue messagingQueue = MessagingQueue.getMessageQueueInstance();
        List<MessageResponse> messageResponseList = messagingQueue.receiveMessage(dataSource);
        return Response.status(Response.Status.OK).entity(messageResponseList).build();
    }
}

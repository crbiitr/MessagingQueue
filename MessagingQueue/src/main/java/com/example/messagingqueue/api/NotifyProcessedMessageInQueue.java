package com.example.messagingqueue.api;

import com.example.messagingqueue.core.MessagingQueue;
import com.example.messagingqueue.datasource.DataSource;
import com.example.messagingqueue.datasource.DataSourceFactory;
import com.example.messagingqueue.model.request.CreateMessageRequest;
import org.skife.jdbi.v2.DBI;

import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by Chetan
 */
public class NotifyProcessedMessageInQueue {
    private DBI jdbi;
    private DataSource dataSource;
    List<CreateMessageRequest> messageRequestList;

    public NotifyProcessedMessageInQueue(DBI jdbi, List<CreateMessageRequest> messageRequestList) {
        this.jdbi = jdbi;
        dataSource = DataSourceFactory.getDataSource("MySQL", jdbi);
        this.messageRequestList = messageRequestList;
    }

    public Response notifyAndUpdateProcessedMessages() {
        MessagingQueue messagingQueue = MessagingQueue.getMessageQueueInstance();
        messagingQueue.notifyAndUpdateProcessedMessages(messageRequestList, dataSource);
        return Response.status(Response.Status.OK).build();
    }
}

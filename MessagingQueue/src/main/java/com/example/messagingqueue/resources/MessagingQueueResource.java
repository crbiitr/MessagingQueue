package com.example.messagingqueue.resources;


import com.example.messagingqueue.api.ConsumeMessageFromQueue;
import com.example.messagingqueue.api.NotifyProcessedMessageInQueue;
import com.example.messagingqueue.api.ProduceMessageInQueue;
import com.example.messagingqueue.model.request.CreateMessageRequest;
import org.skife.jdbi.v2.DBI;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by Chetan
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MessagingQueueResource {
    final DBI jdbi;

    public MessagingQueueResource(DBI jdbi) {
        this.jdbi = jdbi;
    }


    @GET
    @Path("/receive")
    public Response receive() {
        ConsumeMessageFromQueue consumeMessageFromQueue = new ConsumeMessageFromQueue(jdbi);
        Response response = consumeMessageFromQueue.consumeMessagesFromQueue();
        return response;
    }

    @POST
    @Path("/send")
    public Response send() {
        ProduceMessageInQueue produceMessageInQueue = new ProduceMessageInQueue(jdbi);
        Response response = produceMessageInQueue.addMessageInQueue();
        return response;
    }

    @POST
    @Path("/notify")
    public Response notify(List<CreateMessageRequest> createMessageRequestList) {
        NotifyProcessedMessageInQueue notifyProcessedMessageCommand = new NotifyProcessedMessageInQueue(jdbi, createMessageRequestList);
        Response response = notifyProcessedMessageCommand.notifyAndUpdateProcessedMessages();
        return response;
    }
}

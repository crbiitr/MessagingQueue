package com.example.messagingqueue.datasource;


import com.example.messagingqueue.db.MessageForQueueDao;
import com.example.messagingqueue.model.request.CreateMessageRequest;
import com.example.messagingqueue.model.response.CreateMessageResponse;
import com.example.messagingqueue.model.response.MessageResponse;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.skife.jdbi.v2.DBI;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Chetan
 */
@Slf4j
@Setter
@NoArgsConstructor
public class MySQLDataBaseDataSource implements DataSource {
    private DBI jdbi;
    private static final int AUTO_RESET_TIME = 30000;  // in MS

    public MySQLDataBaseDataSource(DBI jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public CreateMessageResponse sendMessage(CreateMessageRequest messageRequest) {
        MessageForQueueDao dao = jdbi.onDemand(MessageForQueueDao.class);
        dao.addNewMessage(messageRequest);
        CreateMessageResponse createMessageResponse = new CreateMessageResponse();
        createMessageResponse.setMessageId(messageRequest.getMessageId());
        return createMessageResponse;
    }

    @Override
    public List<MessageResponse> receiveMessage() {
        MessageForQueueDao dao = jdbi.onDemand(MessageForQueueDao.class);
        List<MessageResponse> messageResponseList = dao.getMessagesFromQueue();
        dao.updateMessageInQueue(messageResponseList);
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                log.info("Auto resetting message after " + AUTO_RESET_TIME + " second !");
                dao.resetMessageInQueue(messageResponseList);
                this.cancel();
                timer.cancel();
            }
        };
        timer.schedule(timerTask, AUTO_RESET_TIME, AUTO_RESET_TIME);
        return messageResponseList;
    }

    @Override
    public void notifyAndUpdateProcessedMessages(List<CreateMessageRequest> updateMessageRequestList) {
        MessageForQueueDao messageDAO = jdbi.onDemand(MessageForQueueDao.class);
        messageDAO.updateMessageAfterNotification(updateMessageRequestList);
    }
}

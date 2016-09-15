package com.example.messagingqueue.db;

import com.example.messagingqueue.model.request.CreateMessageRequest;
import com.example.messagingqueue.model.response.MessageResponse;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlBatch;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

/**
 * Created by Chetan
 */
public interface MessageForQueueDao {
    public static final String TABLE = "message_queue";

    @SqlUpdate("insert into " + TABLE + " (id,processing,processed) values (:request.messageId, 0,0)")
    void addNewMessage(@BindBean("request") CreateMessageRequest createMessageRequest);


    @RegisterMapper(MessageResponse.MessageMapper.class)
    @SqlQuery("select * from " + TABLE + " where processed =0 or processing=0 limit 10")
    List<MessageResponse> getMessagesFromQueue();

    @SqlBatch("update " + TABLE + " set  processed =0, processing =1 where id =:responseList.messageId")
    void updateMessageInQueue(@BindBean("responseList") List<MessageResponse> messageResponseList);

    @SqlBatch("update " + TABLE + " set  processed =0, processing =0 where id =:responseList.messageId")
    void resetMessageInQueue(@BindBean("responseList") List<MessageResponse> messageResponseList);

    @SqlBatch("delete from " + TABLE + " where id =:requestList.messageId")
    void updateMessageAfterNotification(@BindBean("requestList") List<CreateMessageRequest> createMessageRequestList);
}

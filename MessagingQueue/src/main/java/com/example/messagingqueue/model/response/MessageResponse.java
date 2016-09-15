package com.example.messagingqueue.model.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Chetan
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageResponse {
    private String messageId;

    public static class MessageMapper implements ResultSetMapper<MessageResponse> {

        @Override
        public MessageResponse map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {

            return new MessageResponse(resultSet.getString("id"));
        }
    }

}

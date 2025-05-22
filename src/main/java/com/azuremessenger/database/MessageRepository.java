package com.azuremessenger.database;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.azuremessenger.model.Message;
import java.util.List;

public interface MessageRepository extends CosmosRepository<Message, String> {
    List<Message> findMessagesByChatId(String chatId);
}

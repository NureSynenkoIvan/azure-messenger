package com.azuremessenger.database;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.azuremessenger.model.Chat;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends CosmosRepository<Chat, String> {
    List<Chat> findByParticipantsContaining(String userId);

    Chat getChatByChatId(String chatId);

    List<Chat> findByUserId(String userId);
}
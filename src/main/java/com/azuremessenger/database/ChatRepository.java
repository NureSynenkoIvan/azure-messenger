package com.azuremessenger.database;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.azuremessenger.model.Chat;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends CosmosRepository<Chat, String> {
    List<Chat> findByParticipantsContains(List<String> participants);

    Chat getChatById(String chatId);

    List<Chat> findByParticipantsContaining(List<String> participants);
}
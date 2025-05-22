package com.azuremessenger;
import com.azuremessenger.database.ChatRepository;
import com.azuremessenger.database.MessageRepository;
import com.azuremessenger.database.UserRepository;
import com.azuremessenger.model.Chat;
import com.azuremessenger.model.Message;
import com.azuremessenger.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Timestamp;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ChatService {
    private UserRepository userRepository;

    private ChatRepository chatRepository;

    private MessageRepository messageRepository;

    @Autowired
    public ChatService(UserRepository userRepository, ChatRepository chatRepository, MessageRepository messageRepository) {\
        this.userRepository = userRepository;
        this.chatRepository = chatRepository;
        this.messageRepository = messageRepository;

        initializeSampleData();
    }

    public void initializeSampleData() {
        User user1 = new User("user123", "john_doe", "john@example.com", "hashed_password", "url1", "Hello, I'm John", new ArrayList<>());
        User user2 = new User("user456", "jane_doe", "jane@example.com", "hashed_password", "url2", "Hello, I'm Jane", new ArrayList<>());
        userRepository.saveAll(List.of(user1, user2));

        String chatId = UUID.randomUUID().toString();
        Chat chat = new Chat(chatId, List.of("user123", "user456"), "John and Jane", OffsetDateTime.now(), OffsetDateTime.now(), "one-on-one");
        chatRepository.save(chat);

        user1.setChats(List.of(chatId));
        user2.setChats(List.of(chatId));
        userRepository.saveAll(List.of(user1, user2));

        Message message = new Message("1", chatId, "user123", "Hello!", new Date(), null);
        messageRepository.save(message);
    }

    public Chat getChat (String userId, String chatId) {
        Chat chat = chatRepository.findById(chatId).orElse(null);
        if (chat == null ||
                (! chat.getParticipants().contains(userId))) {
            throw new IllegalArgumentException("Chat does not exist");
        }
        return chat;
    }
}
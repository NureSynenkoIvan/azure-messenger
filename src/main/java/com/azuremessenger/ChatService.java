package com.azuremessenger;

import com.azure.cosmos.implementation.guava25.collect.Lists;
import com.azuremessenger.database.ChatRepository;
import com.azuremessenger.database.MessageRepository;
import com.azuremessenger.database.UserRepository;
import com.azuremessenger.model.Chat;
import com.azuremessenger.model.Message;
import com.azuremessenger.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Timestamp;
import java.time.OffsetDateTime;
import java.util.*;

@Service
public class ChatService {
    private UserRepository userRepository;

    private ChatRepository chatRepository;

    private MessageRepository messageRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public ChatService(UserRepository userRepository,
                       ChatRepository chatRepository,
                       MessageRepository messageRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.chatRepository = chatRepository;
        this.messageRepository = messageRepository;
        this.passwordEncoder = passwordEncoder;

        initializeSampleData();
    }

    public void initializeSampleData() {
        User user1 = new User("user123", "john_doe", "john@example.com", "hashed_password", "url1", "Hello, I'm John", new ArrayList<>());
        User user2 = new User("user456", "jane_doe", "jane@example.com", "hashed_password", "url2", "Hello, I'm Jane", new ArrayList<>());
        userRepository.saveAll(List.of(user1, user2));

        String chatId = UUID.randomUUID().toString();
        Chat chat = new Chat(chatId, List.of("user123", "user456"), "John and Jane", (new Date()), (new Date()), "one-on-one");
        chatRepository.save(chat);

        user1.setChats(List.of(chatId));
        user2.setChats(List.of(chatId));
        userRepository.saveAll(List.of(user1, user2));

        Message message = new Message("1", chatId, "user123", "Hello!", new Date(), null);
        messageRepository.save(message);
    }

    public Chat getChat(String userId, String chatId) {
        Chat chat = chatRepository.findById(chatId).orElse(null);
        if (chat == null ||
                (!chat.getParticipants().contains(userId))) {
            throw new IllegalArgumentException("Chat does not exist");
        }
        return chat;
    }

    public User getUser(String userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public List<Message> getMessagesByChat(String chatId) {
        return messageRepository.findMessagesByChatId(chatId);
    }

    public List<Chat> getChatsForUser(String userId) {
        List<Chat> chats = Lists.newArrayList(chatRepository.findAll()); //findByParticipantsContaining(Collections.singletonList(userId));
        return chats.stream().filter(chat -> chat.getParticipants().contains(userId)).toList();
    }

    public void postChat(Chat chat) {
        chat.setId(createChatId(chat));
        chat.setCreatedAt(new Date());
        chat.setLastMessageAt(new Date());
        chatRepository.save(chat);
    }

    private String createChatId(Chat chat) {
        if(chat.getType() == "direct") {
            return "chat_" + chat.getParticipants().get(0) + "_" + chat.getParticipants().get(1);
        }
        return "groupchat_" + chat.getParticipants().get(0);
    }

    public void updateChat(Chat chat) {
        chatRepository.delete(Objects.requireNonNull(chatRepository.findById(chat.getId()).orElse(null)));
        chatRepository.save(chat);
    }

    public Chat validateChat(String userId, String chatId) throws IllegalArgumentException {
        Chat chat = getChat(userId, chatId);
        if (chat == null) {
            throw new IllegalArgumentException("Chat does not exist");
        }
        validateChat(userId, chat);
        return chat;
    }

    public void validateChat(String userId, Chat chat) throws IllegalArgumentException {
        if (chat == null || (!chat.getParticipants().contains(userId)))
            throw new IllegalArgumentException("Chat does not exist");
    }

    public void postMessage(Chat chat, Message message) {
        message.setId(UUID.randomUUID().toString());
        message.setTime(new Date());
        messageRepository.save(message);
        chat.setLastMessageAt(message.getTime());
        this.updateChat(chat);
    }

    public List<User> getUsersForChat(String chatId) {
        Chat chat = chatRepository.findById(chatId).orElse(null);
        if (chat == null) {
            throw new IllegalArgumentException("Chat does not exist");
        }
        List<String> ids = chat.getParticipants();
        List<User> users = new ArrayList<>();
        for (String id : ids) {
            users.add(getUser(id));
        }
        return users;
    }

    public void deleteChat(String userId, String chatId) {
        Chat chat = validateChat(userId, chatId);
        chatRepository.delete(chat);
    }

    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

    public void deleteMessage(String userId, String chatId, String messageId) {
        Message message = messageRepository.findById(messageId).orElse(null);
        if (message == null) {
            throw new IllegalArgumentException("Message does not exist");
        }
        Chat chat = validateChat(userId, chatId);
        if (message.getChatId().equals(chat.getId())) {
            if (message.getSenderId().equals(userId)) {
                messageRepository.deleteById(messageId);
            } else {
                throw new IllegalArgumentException("Trying to delete a message from different user");
            }
        } else {
            throw new IllegalArgumentException("Trying to access message from a different chat");
        }
    }

    public void createUser(User user) {
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        userRepository.save(user);
    }

    public boolean checkPassword(User user, String password) {
        return passwordEncoder.matches(password, user.getPasswordHash());
    }
}
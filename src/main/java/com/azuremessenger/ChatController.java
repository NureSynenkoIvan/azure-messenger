package com.azuremessenger;

import com.azuremessenger.model.Chat;
import com.azuremessenger.model.Message;
import com.azuremessenger.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ChatController {
    @Autowired
    private ChatService chatService;

    @GetMapping("/user")
    public User getUser(@RequestParam("username") String username) {
        return chatService.getUserByUsername(username);
    }

    @GetMapping("user/chats")
    public List<Chat> getChats(@RequestParam("userId") String userId) {
            return chatService.getChatsForUser(userId);
    }

    @PostMapping("/user/chats")
    public ResponseEntity addChat(@RequestParam("userId") String userId, @RequestBody Chat chat) {
        try {
            chatService.postChat(chat);
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/user/chats")
    public ResponseEntity updateChat(@RequestParam("userId") String userId, @RequestBody Chat chat) {
        try {
            chatService.validateChat(userId, chat);
            chatService.updateChat(chat);
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/user")
    public ResponseEntity deleteUser(
            @RequestParam("userId") String userId) {
        try {
            chatService.deleteUser(userId);
            return ResponseEntity.ok().build();
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/chat")
    public Chat getChat(@RequestParam("userId") String userId, @RequestParam("chatId") String chatId) {
        return chatService.getChat(userId, chatId);
    }

    @GetMapping("chat/messages")
    public List<Message> getMessages(@RequestParam("chatId") String chatId) {
        return chatService.getMessagesByChat(chatId);
    }

    @GetMapping("/chat/users")
    public List<User> getUsers(@RequestParam("userId") String chatId) {
        return chatService.getUsersForChat(chatId);
    }

    @PostMapping("chat/messages")
    public ResponseEntity addMessage(
            @RequestParam("userId") String userId,
            @RequestParam("chatId") String chatId,
            @RequestBody Message message) {
        try {
            Chat chat = chatService.getChat(userId, chatId);
            chatService.postMessage(chat, message);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Can't add message");
        }
    }

    @DeleteMapping("/chat")
    public ResponseEntity deleteChat(
            @RequestParam("userId") String userId,
            @RequestParam("chatId") String chatId) {
        try {
            chatService.deleteChat(userId, chatId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/message")
    public ResponseEntity deleteMessage(@RequestParam("userId") String userId,
                                        @RequestParam("chatId") String chatId,
                                        @RequestParam("messageId") String messageId) {
        try {
            chatService.deleteMessage(userId, chatId, messageId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
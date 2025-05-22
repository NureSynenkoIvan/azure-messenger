package com.azuremessenger;

import com.azuremessenger.model.Chat;
import com.azuremessenger.model.Message;
import com.azuremessenger.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ChatController {
    @Autowired
    private ChatService chatService;

    @GetMapping("/chat")
    public Chat getChat(@RequestParam("userId") String userId, @RequestParam("chatId") String chatId) {
        return chatService.getChat(userId, chatId);
    }
}
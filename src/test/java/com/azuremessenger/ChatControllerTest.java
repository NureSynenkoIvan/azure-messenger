// ChatControllerTest.java
package com.azuremessenger;

import com.azuremessenger.model.Chat;
import com.azuremessenger.model.Message;
import com.azuremessenger.model.User;
import com.azuremessenger.ChatService; // Assuming ChatService is in the same package or imported

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ChatControllerTest {

    @Mock
    private ChatService chatService;

    @InjectMocks
    private ChatController chatController;



    /**
     * Test case for getChats endpoint - successful retrieval of chats for a user.
     */
    @Test
    void testGetChats_Success() {
        // Arrange
        String userId = "testUser123";
        Chat chat1 = new Chat();
        chat1.setId("chat1");
        Chat chat2 = new Chat();
        chat2.setId("chat2");
        List<Chat> mockChats = Arrays.asList(chat1, chat2);

        when(chatService.getChatsForUser(userId)).thenReturn(mockChats);

        // Act
        List<Chat> result = chatController.getChats(userId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("chat1", result.get(0).getId());
        verify(chatService, times(1)).getChatsForUser(userId);
    }


    /**
     * Test case for updateChat endpoint - successful chat update.
     */
    @Test
    void testUpdateChat_Success() {
        // Arrange
        String userId = "testUser123";
        Chat existingChat = new Chat();
        existingChat.setId("existingChatId");

        doNothing().when(chatService).validateChat(anyString(), any(Chat.class));
        doNothing().when(chatService).updateChat(any(Chat.class));

        // Act
        ResponseEntity result = chatController.updateChat(userId, existingChat);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(chatService, times(1)).validateChat(userId, existingChat);
        verify(chatService, times(1)).updateChat(existingChat);
    }

    /**
     * Test case for updateChat endpoint - chat update fails due to exception.
     */
    @Test
    void testUpdateChat_BadRequest() {
        // Arrange
        String userId = "testUser123";
        Chat existingChat = new Chat();
        existingChat.setId("existingChatId");
        String errorMessage = "Chat not found";

        doThrow(new RuntimeException(errorMessage))
                .when(chatService).updateChat(any(Chat.class)); // Can throw from validateChat or updateChat

        // Act
        ResponseEntity result = chatController.updateChat(userId, existingChat);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals(errorMessage, result.getBody());
        verify(chatService, times(1)).validateChat(userId, existingChat); // validateChat is called before updateChat
        verify(chatService, times(1)).updateChat(existingChat); // updateChat is called as it's mocked to throw
    }

    /**
     * Test case for deleteUser endpoint - successful user deletion.
     */
    @Test
    void testDeleteUser_Success() {
        // Arrange
        String userId = "userToDelete";
        doNothing().when(chatService).deleteUser(anyString());

        // Act
        ResponseEntity result = chatController.deleteUser(userId);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(chatService, times(1)).deleteUser(userId);
    }

    /**
     * Test case for deleteUser endpoint - user deletion fails due to exception.
     */
    @Test
    void testDeleteUser_BadRequest() {
        // Arrange
        String userId = "userToDelete";
        String errorMessage = "User not found for deletion";
        doThrow(new IllegalStateException(errorMessage)).when(chatService).deleteUser(anyString());

        // Act
        ResponseEntity result = chatController.deleteUser(userId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals(errorMessage, result.getBody());
        verify(chatService, times(1)).deleteUser(userId);
    }


    @Test
    void testDeleteChat_Success() {
        // Arrange
        String userId = "user1";
        String chatId = "chatToDelete";
        doNothing().when(chatService).deleteChat(anyString(), anyString());

        // Act
        ResponseEntity result = chatController.deleteChat(userId, chatId);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(chatService, times(1)).deleteChat(userId, chatId);
    }


    @Test
    void testDeleteChat_BadRequest() {
        // Arrange
        String userId = "user1";
        String chatId = "chatToDelete";
        String errorMessage = "Chat not found for deletion";
        doThrow(new IllegalStateException(errorMessage)).when(chatService).deleteChat(anyString(), anyString());

        // Act
        ResponseEntity result = chatController.deleteChat(userId, chatId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals(errorMessage, result.getBody());
        verify(chatService, times(1)).deleteChat(userId, chatId);
    }

    @Test
    void testDeleteMessage_Success() {
        // Arrange
        String userId = "user1";
        String chatId = "chat123";
        String messageId = "msgToDelete";
        doNothing().when(chatService).deleteMessage(anyString(), anyString(), anyString());

        // Act
        ResponseEntity result = chatController.deleteMessage(userId, chatId, messageId);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(chatService, times(1)).deleteMessage(userId, chatId, messageId);
    }
    @Test
    void testDeleteMessage_BadRequest() {
        // Arrange
        String userId = "user1";
        String chatId = "chat123";
        String messageId = "msgToDelete";
        String errorMessage = "Message not found or unauthorized";
        doThrow(new RuntimeException(errorMessage))
                .when(chatService).deleteMessage(anyString(), anyString(), anyString());

        // Act
        ResponseEntity result = chatController.deleteMessage(userId, chatId, messageId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals(errorMessage, result.getBody());
        verify(chatService, times(1)).deleteMessage(userId, chatId, messageId);
    }


}

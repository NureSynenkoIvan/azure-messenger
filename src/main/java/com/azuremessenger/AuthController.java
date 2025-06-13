package com.azuremessenger;

import com.azuremessenger.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private ChatService chatService;

    /*
    @GetMapping
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("Hello World");
    }*/

    @PostMapping()
    public ResponseEntity registerOrLogin(@RequestParam("username") String username, @RequestBody User user) {
        try {
                chatService.createUser(user);
                return ResponseEntity.ok().build();

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity login(@RequestParam("username") String username, @RequestParam("password") String password) {
        try {
            User user = chatService.getUserByUsername(username);
            if(chatService.checkPassword(user, password)) {
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.badRequest().build();
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



}

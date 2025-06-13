package com.azuremessenger.model;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import org.springframework.data.annotation.Id;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;

@Container(containerName = "Chats")
public class Chat {
    @Id
    private String id;
    private List<String> participants;
    private String name;
    private Date createdAt;
    private Date lastMessageAt;
    private String type;

    public Chat() {}

    public Chat(String id, List<String> participants, String name, Date createdAt, Date lastMessageAt, String type) {
        this.id = id;
        this.participants = participants;
        this.name = name;
        this.createdAt = createdAt;
        this.lastMessageAt = lastMessageAt;
        this.type = type;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public List<String> getParticipants() { return participants; }
    public void setParticipants(List<String> participants) { this.participants = participants; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public Date getLastMessageAt() { return lastMessageAt; }
    public void setLastMessageAt(Date lastMessageAt) { this.lastMessageAt = lastMessageAt; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
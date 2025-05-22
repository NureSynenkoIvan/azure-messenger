package com.azuremessenger.database;
import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.azuremessenger.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CosmosRepository<User, String> {
    User findByUsername(String username);
}
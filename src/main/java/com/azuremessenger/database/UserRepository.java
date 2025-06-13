package com.azuremessenger.database;
import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.azuremessenger.model.User;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CosmosRepository<User, String> {
    Optional<User> findByUsername(String username);
}
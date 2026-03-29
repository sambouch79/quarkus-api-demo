package io.github.sambouch79.demo.domain.port.out;

import io.github.sambouch79.demo.domain.model.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Port sortant — contrat vers l'infrastructure de persistance.
 */
public interface UserPort {

  Optional<User> findByUuid(UUID uuid);

  List<User> findPage(int page, int size, String sortField, boolean ascending);

  long countAll();

  User persist(User user);

  void update(User user);

  void delete(User user);
}

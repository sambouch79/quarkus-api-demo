package io.github.sambouch79.demo.domain.port.in;

import io.github.sambouch79.demo.domain.model.User;
import java.util.List;
import java.util.UUID;

/**
 * Port entrant — contrat du use case User.
 * Définit les opérations métier exposées à la couche API.
 */
public interface UserUseCase {

  User findByUuid(UUID uuid);

  List<User> findPage(int page, int size, String sortField, boolean ascending);

  long countAll();

  User create(User user);

  void updateByUuid(User user);

  void deleteByUuid(UUID uuid);
}

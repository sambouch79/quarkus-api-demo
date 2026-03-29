package io.github.sambouch79.demo.domain.usecase;

import io.github.sambouch79.demo.domain.model.User;
import io.github.sambouch79.demo.domain.port.in.UserUseCase;
import io.github.sambouch79.demo.domain.port.out.UserPort;
import io.github.sambouch79.demo.shared.exception.ResourceNotFoundException;

import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

/** Implémentation du use case User. */
@Slf4j
public class UserUseCaseImpl implements UserUseCase {

  private final UserPort userPort;
  private static final String NOT_FOUND_MSG = "User avec UUID '%s' introuvable";

  public UserUseCaseImpl(UserPort userPort) {
    this.userPort = userPort;
  }

  @Override
  public User findByUuid(UUID uuid) {
    if (uuid == null) throw new IllegalArgumentException("L'UUID ne peut pas être null");
    return userPort.findByUuid(uuid)
        .orElseThrow(() -> new ResourceNotFoundException(String.format(NOT_FOUND_MSG, uuid)));
  }

  @Override
  public List<User> findPage(int page, int size, String sortField, boolean ascending) {
    return userPort.findPage(page, size, sortField, ascending);
  }

  @Override
  public long countAll() {
    return userPort.countAll();
  }

  @Override
  public User create(User user) {
    log.debug("create user nom={}", user.nom());
    return userPort.persist(user);
  }

  @Override
  public void updateByUuid(User user) {
    if (user.uuid() == null) throw new IllegalArgumentException("L'UUID ne peut pas être null");
    userPort.findByUuid(user.uuid())
        .orElseThrow(() -> new ResourceNotFoundException(String.format(NOT_FOUND_MSG, user.uuid())));
    userPort.update(user);
  }

  @Override
  public void deleteByUuid(UUID uuid) {
    if (uuid == null) throw new IllegalArgumentException("L'UUID ne peut pas être null");
    User user = userPort.findByUuid(uuid)
        .orElseThrow(() -> new ResourceNotFoundException(String.format(NOT_FOUND_MSG, uuid)));
    userPort.delete(user);
  }
}

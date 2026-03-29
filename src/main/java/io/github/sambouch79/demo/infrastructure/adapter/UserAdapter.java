package io.github.sambouch79.demo.infrastructure.adapter;

import io.github.sambouch79.demo.infrastructure.persistence.entity.UserEntity;
import io.github.sambouch79.demo.infrastructure.persistence.mapper.UserEntityMapper;
import io.github.sambouch79.demo.domain.model.User;
import io.github.sambouch79.demo.domain.port.out.UserPort;
import io.github.sambouch79.demo.infrastructure.persistence.repository.UserRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/** Adaptateur infrastructure : implémente {@link UserPort} via Panache. */
@ApplicationScoped
public class UserAdapter implements UserPort {

  private final UserRepository repository;
  private final UserEntityMapper mapper;

  @Inject
  public UserAdapter(UserRepository repository, UserEntityMapper mapper) {
    this.repository = repository;
    this.mapper     = mapper;
  }

  @Override
  public Optional<User> findByUuid(UUID uuid) {
    return repository.findByUuid(uuid).map(mapper::toDomain);
  }

  @Override
  public List<User> findPage(int page, int size, String sortField, boolean ascending) {
    Sort sort = ascending ? Sort.ascending(sortField) : Sort.descending(sortField);
    return repository.findPage(page, size, sort).stream().map(mapper::toDomain).toList();
  }

  @Override
  public long countAll() {
    return repository.countAll();
  }

  @Override
  @Transactional
  public User persist(User user) {
    UserEntity entity = mapper.toEntity(user);
    repository.persist(entity);
    repository.flush();
    return mapper.toDomain(entity);
  }

  @Override
  @Transactional
  public void update(User user) {
    UserEntity entity = mapper.toEntity(user);
    repository.getEntityManager().merge(entity);
    repository.flush();
  }

  @Override
  @Transactional
  public void delete(User user) {
    UserEntity entity = repository.findByUuid(user.uuid())
        .orElseThrow(() -> new IllegalArgumentException("Entity not found: " + user.uuid()));
    repository.delete(entity);
  }
}

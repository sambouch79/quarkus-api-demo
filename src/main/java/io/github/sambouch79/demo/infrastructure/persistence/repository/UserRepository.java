package io.github.sambouch79.demo.infrastructure.persistence.repository;

import io.github.sambouch79.demo.infrastructure.persistence.entity.UserEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/** Repository Panache pour {@link UserEntity}. */
@ApplicationScoped
public class UserRepository implements PanacheRepositoryBase<UserEntity, Long> {

  public Optional<UserEntity> findByUuid(UUID uuid) {
    return find("uuid", uuid).firstResultOptional();
  }

  public List<UserEntity> findPage(int page, int size, Sort sort) {
    return findAll(sort).page(page, size).list();
  }

  public long countAll() {
    return count();
  }

  public void flush() {
    getEntityManager().flush();
  }
}

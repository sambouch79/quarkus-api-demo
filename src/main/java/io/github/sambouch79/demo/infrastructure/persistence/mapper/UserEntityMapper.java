package io.github.sambouch79.demo.infrastructure.persistence.mapper;

import io.github.sambouch79.demo.infrastructure.persistence.entity.StatutJuridiqueEntity;
import io.github.sambouch79.demo.infrastructure.persistence.entity.UserEntity;
import io.github.sambouch79.demo.domain.model.StatutJuridiqueUser;
import io.github.sambouch79.demo.domain.model.StatutUser;
import io.github.sambouch79.demo.domain.model.User;
import io.github.sambouch79.demo.infrastructure.persistence.entity.StatutUserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/** Mapper MapStruct entre {@link User} (domaine) et {@link UserEntity} (JPA). */
@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserEntityMapper {

  @Mapping(target = "statut",         source = "statut")
  @Mapping(target = "statutJuridique", source = "statutJuridique")
  User toDomain(UserEntity entity);

  @Mapping(target = "statut",         source = "statut")
  @Mapping(target = "statutJuridique", source = "statutJuridique")
  UserEntity toEntity(User domain);

  default StatutUser toStatutDomain(StatutUserEntity e) {
    return e == null ? null : StatutUser.valueOf(e.name());
  }

  default StatutUserEntity toStatutEntity(StatutUser s) {
    return s == null ? null : StatutUserEntity.valueOf(s.name());
  }

  default StatutJuridiqueUser toStatutJuridiqueDomain(StatutJuridiqueEntity e) {
    return e == null ? null : StatutJuridiqueUser.valueOf(e.name());
  }

  default StatutJuridiqueEntity toStatutJuridiqueEntity(StatutJuridiqueUser s) {
    return s == null ? null : StatutJuridiqueEntity.valueOf(s.name());
  }
}

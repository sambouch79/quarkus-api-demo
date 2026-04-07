package io.github.sambouch79.demo.api.mapper;

import io.github.sambouch79.demo.api.dto.UserDTO;
import io.github.sambouch79.demo.domain.model.StatutJuridiqueUser;
import io.github.sambouch79.demo.domain.model.StatutUser;
import io.github.sambouch79.demo.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper MapStruct entre {@link User} (domaine) et {@link UserDTO} (API).
 *
 * <p>Trois cas d'usage :
 * <ul>
 *   <li>Lecture : domaine → DTO ({@link #toDto})</li>
 *   <li>Création : DTO → domaine ({@link #toModelOnCreate})</li>
 *   <li>Patch : DTO + domaine existant → domaine mis à jour ({@link #updateModelFromDto})</li>
 * </ul>
 */
@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserMapper {

  // ===== Domaine → DTO =====

  @Mapping(target = "statut",         expression = "java(model.statut() != null ? model.statut().name() : null)")
  @Mapping(target = "statutJuridique", expression = "java(model.statutJuridique() != null ? model.statutJuridique().name() : null)")
  UserDTO toDto(User model);

  // ===== DTO → Domaine (CREATE) =====

  @Mapping(target = "id",               ignore = true)
  @Mapping(target = "uuid",             ignore = true)
  @Mapping(target = "dateCreation",     ignore = true)
  @Mapping(target = "dateModification", ignore = true)
  @Mapping(target = "modifiePar",       ignore = true)
  @Mapping(target = "statut",           expression = "java(dto.getStatut() != null ? io.github.sambouch79.demo.domain.model.StatutUser.valueOf(dto.getStatut()) : io.github.sambouch79.demo.domain.model.StatutUser.ACTIF)")
  @Mapping(target = "statutJuridique",  expression = "java(io.github.sambouch79.demo.domain.model.StatutJuridiqueUser.valueOf(dto.getStatutJuridique()))")
  User toModelOnCreate(UserDTO dto);

  // ===== PATCH : fusion DTO + domaine existant =====

  /**
   * Applique un PATCH : seuls les champs non-null du DTO écrasent le domaine existant.
   * Les champs immuables (uuid, id, creePar, dateCreation) sont toujours conservés.
   */
  default User updateModelFromDto(UserDTO dto, User existing) {
    return User.builder()
        // immuables
        .id(existing.id())
        .uuid(existing.uuid())
        .creePar(existing.creePar())
        .dateCreation(existing.dateCreation())
        .dateModification(existing.dateModification())
        // patchables
        .nom(dto.getNom()                   != null ? dto.getNom()                   : existing.nom())
        .prenom(dto.getPrenom()             != null ? dto.getPrenom()                : existing.prenom())
        .siret(dto.getSiret()               != null ? dto.getSiret()                 : existing.siret())
        .numeroFiness(dto.getNumeroFiness() != null ? dto.getNumeroFiness()          : existing.numeroFiness())
        .statut(dto.getStatut()             != null ? StatutUser.valueOf(dto.getStatut()) : existing.statut())
        .statutJuridique(dto.getStatutJuridique() != null
            ? StatutJuridiqueUser.valueOf(dto.getStatutJuridique()) : existing.statutJuridique())
        .affiliationCollectifAssociatif(dto.getAffiliationCollectifAssociatif() != null
            ? dto.getAffiliationCollectifAssociatif() : existing.affiliationCollectifAssociatif())
        .outilTelegestion(dto.getOutilTelegestion() != null
            ? dto.getOutilTelegestion() : existing.outilTelegestion())
        .modifiePar(dto.getModifiePar()     != null ? dto.getModifiePar()            : existing.modifiePar())
        .build();
  }
}

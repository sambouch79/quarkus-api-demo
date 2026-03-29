package io.github.sambouch79.demo.infrastructure.persistence.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

/** Entité JPA pour la table {@code users}. */
@Entity
@Table(name = "users")
@Getter
@Setter
public class UserEntity extends PanacheEntityBase {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true, updatable = false)
  private UUID uuid;

  @Column(nullable = false, length = 100)
  private String nom;

  @Column(nullable = false, length = 100)
  private String prenom;

  @Column(nullable = false, length = 14)
  private String siret;

  @Column(name = "numero_finess", length = 14)
  private String numeroFiness;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private StatutUserEntity statut;

  @Enumerated(EnumType.STRING)
  @Column(name = "statut_juridique", nullable = false)
  private StatutJuridiqueEntity statutJuridique;

  @Column(name = "affiliation_collectif_associatif")
  private Boolean affiliationCollectifAssociatif;

  @Column(name = "outil_telegestion")
  private Boolean outilTelegestion;

  @Column(name = "cree_par", nullable = false, length = 100)
  private String creePar;

  @Column(name = "modifie_par", length = 100)
  private String modifiePar;

  @Column(name = "date_creation", nullable = false, updatable = false)
  private LocalDateTime dateCreation;

  @Column(name = "date_modification")
  private LocalDateTime dateModification;

  @PrePersist
  void prePersist() {
    if (uuid == null)         uuid = UUID.randomUUID();
    if (dateCreation == null) dateCreation = LocalDateTime.now();
    if (statut == null)       statut = StatutUserEntity.ACTIF;
  }

  @PreUpdate
  void preUpdate() {
    dateModification = LocalDateTime.now();
  }
}

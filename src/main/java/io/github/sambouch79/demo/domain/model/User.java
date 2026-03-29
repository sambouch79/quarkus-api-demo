package io.github.sambouch79.demo.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Modèle domaine représentant un utilisateur.
 *
 * <p>Volontairement épuré par rapport au domaine Partenaire :
 * pas d'adresse, focus sur l'identité et les identifiants métier.
 */
public record User(
    Long id,
    UUID uuid,
    String nom,
    String prenom,
    String siret,
    String numeroFiness,
    StatutUser statut,
    StatutJuridiqueUser statutJuridique,
    Boolean affiliationCollectifAssociatif,
    Boolean outilTelegestion,
    String creePar,
    String modifiePar,
    LocalDateTime dateCreation,
    LocalDateTime dateModification) {

  public static Builder builder() { return new Builder(); }

  public static class Builder {
    private Long id;
    private UUID uuid;
    private String nom;
    private String prenom;
    private String siret;
    private String numeroFiness;
    private StatutUser statut;
    private StatutJuridiqueUser statutJuridique;
    private Boolean affiliationCollectifAssociatif;
    private Boolean outilTelegestion;
    private String creePar;
    private String modifiePar;
    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;

    public Builder id(Long id)                                             { this.id = id; return this; }
    public Builder uuid(UUID uuid)                                         { this.uuid = uuid; return this; }
    public Builder nom(String nom)                                         { this.nom = nom; return this; }
    public Builder prenom(String prenom)                                   { this.prenom = prenom; return this; }
    public Builder siret(String siret)                                     { this.siret = siret; return this; }
    public Builder numeroFiness(String numeroFiness)                       { this.numeroFiness = numeroFiness; return this; }
    public Builder statut(StatutUser statut)                               { this.statut = statut; return this; }
    public Builder statutJuridique(StatutJuridiqueUser statutJuridique)    { this.statutJuridique = statutJuridique; return this; }
    public Builder affiliationCollectifAssociatif(Boolean v)               { this.affiliationCollectifAssociatif = v; return this; }
    public Builder outilTelegestion(Boolean v)                             { this.outilTelegestion = v; return this; }
    public Builder creePar(String creePar)                                 { this.creePar = creePar; return this; }
    public Builder modifiePar(String modifiePar)                           { this.modifiePar = modifiePar; return this; }
    public Builder dateCreation(LocalDateTime dateCreation)                { this.dateCreation = dateCreation; return this; }
    public Builder dateModification(LocalDateTime dateModification)        { this.dateModification = dateModification; return this; }

    public User build() {
      return new User(id, uuid, nom, prenom, siret, numeroFiness, statut, statutJuridique,
          affiliationCollectifAssociatif, outilTelegestion, creePar, modifiePar,
          dateCreation, dateModification);
    }
  }
}

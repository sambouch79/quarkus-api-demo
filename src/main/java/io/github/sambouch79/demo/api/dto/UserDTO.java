package io.github.sambouch79.demo.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.sambouch79.demo.shared.annotation.ValidNumeroFiness;
import io.github.sambouch79.demo.shared.annotation.ValidSiret;
import jakarta.validation.constraints.*;
import jakarta.validation.groups.Default;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * DTO de transfert pour un utilisateur.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Représentation d'un utilisateur")
public class UserDTO {

  public interface Create extends Default {}
  public interface Update extends Default {}

  @Schema(description = "Identifiant technique interne", readOnly = true, example = "1")
  private Long id;

  @Schema(description = "UUID public de l'utilisateur", readOnly = true,
      example = "11111111-1111-1111-1111-111111111111")
  private UUID uuid;

  @NotBlank(groups = Create.class, message = "Le nom est obligatoire")
  @Size(max = 100, message = "Le nom ne peut pas dépasser 100 caractères")
  @Schema(description = "Nom de famille", example = "Dupont", required = true)
  private String nom;

  @NotBlank(groups = Create.class, message = "Le prénom est obligatoire")
  @Size(max = 100, message = "Le prénom ne peut pas dépasser 100 caractères")
  @Schema(description = "Prénom", example = "Marie", required = true)
  private String prenom;

  @NotBlank(groups = Create.class, message = "Le SIRET est obligatoire")
  @ValidSiret(message = "Le SIRET est invalide", groups = Create.class)
  @Schema(description = "Numéro SIRET (14 chiffres, clé Luhn valide)",
      example = "35600000048004", required = true)
  private String siret;

  @ValidNumeroFiness(optional = true)
  @Schema(description = "Numéro FINESS (9 ou 14 chiffres, optionnel)", example = "123456789")
  private String numeroFiness;

  @NotNull(message = "Le statut juridique est obligatoire")
  @Schema(description = "Statut juridique de la structure",
      example = "SARL", enumeration = {"SARL","SAS","EURL","SA","EI","ASSOCIATION","AUTRE"})
  private String statutJuridique;

  @Schema(description = "Statut actif/inactif", example = "ACTIF",
      enumeration = {"ACTIF", "INACTIF"})
  private String statut;

  @Schema(description = "Affiliation à un collectif associatif", example = "true")
  private Boolean affiliationCollectifAssociatif;

  @Schema(description = "Utilisation d'un outil de télégestion", example = "false")
  private Boolean outilTelegestion;

  @NotBlank(groups = Create.class, message = "L'application créatrice est obligatoire")
  @Size(max = 100)
  @Schema(description = "Identifiant de l'application ayant créé la ressource",
      example = "MON_APP", required = true)
  private String creePar;

  @NotBlank(groups = Update.class, message = "L'application modifiante est obligatoire")
  @Size(max = 100)
  @Schema(description = "Identifiant de l'application ayant modifié la ressource",
      example = "MON_APP")
  private String modifiePar;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Schema(description = "Date de création (générée automatiquement)", readOnly = true)
  private LocalDateTime dateCreation;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Schema(description = "Date de dernière modification (générée automatiquement)", readOnly = true)
  private LocalDateTime dateModification;
}

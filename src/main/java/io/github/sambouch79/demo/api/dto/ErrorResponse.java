package io.github.sambouch79.demo.api.dto;

import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * DTO de réponse d'erreur standardisée.
 *
 * <pre>{@code
 * {
 *   "code": "VALIDATION_ERROR",
 *   "message": "Erreur de validation",
 *   "timestamp": "2026-03-18T10:00:00Z",
 *   "errors": [
 *     { "field": "siret", "message": "Le SIRET est invalide", "rejectedValue": "123" }
 *   ]
 * }
 * }</pre>
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Réponse d'erreur standardisée")
public class ErrorResponse {

  @Schema(description = "Code identifiant le type d'erreur", example = "VALIDATION_ERROR")
  private String code;

  @Schema(description = "Message d'erreur lisible", example = "Erreur de validation")
  private String message;

  @Builder.Default
  @Schema(description = "Horodatage de l'erreur")
  private Instant timestamp = Instant.now();

  @Schema(description = "Détail des violations de validation (optionnel)")
  private List<FieldError> errors;

  public ErrorResponse(String code, String message) {
    this.code      = code;
    this.message   = message;
    this.timestamp = Instant.now();
  }

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Schema(description = "Violation de contrainte sur un champ")
  public static class FieldError {

    @Schema(description = "Nom du champ en erreur", example = "siret")
    private String field;

    @Schema(description = "Message de la violation", example = "Le SIRET est invalide")
    private String message;

    @Schema(description = "Valeur rejetée", example = "123")
    private Object rejectedValue;
  }
}

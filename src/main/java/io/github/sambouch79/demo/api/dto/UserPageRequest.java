package io.github.sambouch79.demo.api.dto;

import io.github.sambouch79.utilscommons.pagination.PageRequest;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.QueryParam;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;

/**
 * Paramètres de pagination et de tri pour l'endpoint User.
 * Étend {@link PageRequest} de quarkus-health-commons.
 */
@Getter
@Setter
public class UserPageRequest extends PageRequest {

  @QueryParam("sortBy")
  @DefaultValue("id")
  @Parameter(description = "Champ de tri", example = "nom",
      schema = @org.eclipse.microprofile.openapi.annotations.media.Schema(
          enumeration = {"id", "nom", "prenom", "siret", "statut"}))
  private String sortBy;
}

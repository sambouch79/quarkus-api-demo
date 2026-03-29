package io.github.sambouch79.demo.shared.util;

import io.github.sambouch79.demo.api.dto.ErrorResponse;
import jakarta.ws.rs.core.MediaType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Regroupe les réponses d'erreur communes à tous les endpoints.
 * À poser sur la classe Resource pour éviter la répétition.
 */
@Retention(RUNTIME)
@Target({TYPE, METHOD})
@APIResponses({
    @APIResponse(responseCode = "400", description = "Requête invalide",
        content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = ErrorResponse.class))),
    @APIResponse(responseCode = "401", description = "Non authentifié — clé API manquante",
        content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = ErrorResponse.class))),
    @APIResponse(responseCode = "403", description = "Accès refusé — clé API invalide",
        content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = ErrorResponse.class))),
    @APIResponse(responseCode = "404", description = "Ressource non trouvée",
        content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = ErrorResponse.class))),
    @APIResponse(responseCode = "500", description = "Erreur interne du serveur",
        content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = ErrorResponse.class)))
})
public @interface ApiResponsesDefault {}

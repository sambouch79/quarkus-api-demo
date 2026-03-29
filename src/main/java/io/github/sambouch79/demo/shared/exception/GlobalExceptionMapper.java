package io.github.sambouch79.demo.shared.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.sambouch79.demo.api.dto.ErrorResponse;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;

/** Gestionnaire global d'exceptions — retourne des erreurs JSON standardisées. */
@Slf4j
@Provider
@Priority(1)
public class GlobalExceptionMapper implements ExceptionMapper<Exception> {

  private static final Set<String> IGNORED_PATHS =
          Set.of("/favicon.ico", "/robots.txt", "/sitemap.xml");

  @Inject MeterRegistry meterRegistry;
  @Context UriInfo uriInfo;

  @Override
  public Response toResponse(Exception exception) {

    if (exception instanceof NotFoundException) {
      String path = uriInfo != null ? uriInfo.getPath() : "";
      if (IGNORED_PATHS.contains("/" + path) || IGNORED_PATHS.contains(path)) {
        return Response.status(Response.Status.NOT_FOUND).build();
      }
    }

    log.error("Exception interceptée", exception);
    meterRegistry.counter("exceptions", "type", exception.getClass().getSimpleName()).increment();

    Throwable root = findRootCause(exception);

    if (root instanceof ConstraintViolationException e) return handleConstraintViolation(e);
    if (root instanceof JsonProcessingException e)      return handleJsonProcessing(e);
    if (root instanceof ResourceNotFoundException e)    return build(Response.Status.NOT_FOUND, "NOT_FOUND", e.getMessage());
    if (root instanceof BusinessException e)            return build(Response.Status.fromStatusCode(e.getHttpStatus()), "BUSINESS_ERROR", e.getMessage());
    if (root instanceof WebApplicationException e)      return handleWebApplication(e);
    if (root instanceof IllegalArgumentException e)     return handleIllegalArgument(e);

    return build(Response.Status.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "Une erreur interne est survenue");
  }

  private Response handleConstraintViolation(ConstraintViolationException ex) {
    List<ErrorResponse.FieldError> errors = ex.getConstraintViolations().stream()
            .map(v -> ErrorResponse.FieldError.builder()
                    .field(lastSegment(v.getPropertyPath().toString()))
                    .message(v.getMessage())
                    .rejectedValue(v.getInvalidValue())
                    .build())
            .toList();
    return Response.status(Response.Status.BAD_REQUEST)
            .entity(ErrorResponse.builder()
                    .code("VALIDATION_ERROR")
                    .message("Erreur de validation")
                    .errors(errors)
                    .build())
            .build();
  }

  private Response handleJsonProcessing(JsonProcessingException ex) {
    log.warn("JSON parsing error: {}", ex.getOriginalMessage());
    return build(Response.Status.BAD_REQUEST, "INVALID_JSON",
            "Corps de la requête invalide : " + ex.getOriginalMessage());
  }

  private Response handleWebApplication(WebApplicationException ex) {
    int    status  = ex.getResponse().getStatus();
    String message = ex.getMessage();
    if (status == 405 && message != null && message.contains("No resource method found")) {
      return build(Response.Status.BAD_REQUEST, "INVALID_REQUEST", "Paramètre manquant ou invalide dans l'URL");
    }
    if (message != null && (message.contains("Unable to extract parameter") || message.contains("PathParam"))) {
      return build(Response.Status.BAD_REQUEST, "INVALID_PARAMETER", "Format de paramètre invalide");
    }
    return build(Response.Status.fromStatusCode(status), "HTTP_" + status,
            message != null ? message : "Erreur HTTP " + status);
  }

  private Response handleIllegalArgument(IllegalArgumentException ex) {
    String msg = ex.getMessage();
    if (msg != null && msg.toLowerCase().contains("uuid")) {
      return build(Response.Status.BAD_REQUEST, "INVALID_PARAMETER", "Format d'identifiant invalide");
    }
    return build(Response.Status.BAD_REQUEST, "INVALID_ARGUMENT", "Argument invalide");
  }

  private Response build(Response.Status status, String code, String message) {
    return Response.status(status)
            .entity(new ErrorResponse(code, message))
            .build();
  }

  private Throwable findRootCause(Throwable t) {
    Throwable current = t;
    while (current.getCause() != null && current.getCause() != current) {
      Throwable cause = current.getCause();
      if (cause instanceof ConstraintViolationException || cause instanceof JsonProcessingException) {
        return cause;
      }
      current = cause;
    }
    return t;
  }

  private String lastSegment(String path) {
    int dot = path.lastIndexOf('.');
    return dot >= 0 ? path.substring(dot + 1) : path;
  }
}


package io.github.sambouch79.demo.api.filter;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import java.util.*;
import java.util.stream.Collectors;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * Filtre JAX-RS d'authentification par clé API.
 *
 * <ul>
 *   <li>Clé absente → {@code 401 Unauthorized}</li>
 *   <li>Clé invalide → {@code 403 Forbidden}</li>
 * </ul>
 *
 * <p>Configuration :
 * <pre>
 * security.api-key.value=demo-secret-key
 * security.api-key.header=X-API-KEY
 * security.api-key.excluded-paths=/q/health,/q/metrics,/q/openapi,/q/swagger-ui
 * </pre>
 */
@Provider
@ApplicationScoped
@Priority(Priorities.AUTHENTICATION)
public class ApiKeyAuthFilter implements ContainerRequestFilter {

  @ConfigProperty(name = "security.api-key.header", defaultValue = "X-API-KEY")
  String headerName;

  @ConfigProperty(name = "security.api-key.value")
  String apiKeyValue;

  @ConfigProperty(name = "security.api-key.excluded-paths",
      defaultValue = "/q/health,/q/metrics,/q/openapi,/q/swagger-ui,/swagger-ui,/api/v1/ping")
  String excludedPathsCsv;

  private Set<String> excludedPaths;

  @PostConstruct
  void init() {
    if (apiKeyValue == null || apiKeyValue.isBlank()) {
      throw new IllegalStateException("security.api-key.value must not be empty");
    }
    excludedPaths = Arrays.stream(excludedPathsCsv.split(","))
        .map(String::trim)
        .filter(s -> !s.isBlank())
        .collect(Collectors.toUnmodifiableSet());
  }

  @Override
  public void filter(ContainerRequestContext ctx) {
    String path = ctx.getUriInfo().getPath();
    if (isExcluded(path)) return;

    String providedKey = getHeaderCaseInsensitive(ctx, headerName);

    if (providedKey == null || providedKey.isBlank()) {
      abort(ctx, Response.Status.UNAUTHORIZED, "API key manquante");
      return;
    }
    if (!Objects.equals(providedKey, apiKeyValue)) {
      abort(ctx, Response.Status.FORBIDDEN, "API key invalide");
    }
  }

  private boolean isExcluded(String path) {
    String normalized = path.endsWith("/") && path.length() > 1
        ? path.substring(0, path.length() - 1) : path;
    return excludedPaths.stream()
        .anyMatch(ex -> normalized.equals(ex) || normalized.startsWith(ex + "/"));
  }

  private static String getHeaderCaseInsensitive(ContainerRequestContext ctx, String name) {
    for (var entry : ctx.getHeaders().entrySet()) {
      if (entry.getKey().equalsIgnoreCase(name)) {
        return ctx.getHeaders().getFirst(entry.getKey());
      }
    }
    return null;
  }

  private static void abort(ContainerRequestContext ctx, Response.Status status, String msg) {
    Map<String, Object> body = new HashMap<>();
    body.put("error", msg);
    body.put("code", status.getStatusCode());
    ctx.abortWith(Response.status(status).entity(body).type("application/json").build());
  }
}

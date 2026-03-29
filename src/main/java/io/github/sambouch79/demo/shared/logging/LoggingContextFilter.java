package io.github.sambouch79.demo.shared.logging;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.MDC;

/** Injecte les informations de contexte dans le MDC pour le logging structuré. */
@Provider
public class LoggingContextFilter implements ContainerRequestFilter {

  @Override
  public void filter(ContainerRequestContext ctx) {
    MDC.put("application", "DEMO-USER-API");
    MDC.put("http_method", ctx.getMethod());
    MDC.put("path",        ctx.getUriInfo().getPath());
    String uuid = ctx.getUriInfo().getPathParameters().getFirst("uuid");
    if (uuid != null) MDC.put("uuid", uuid);
  }
}

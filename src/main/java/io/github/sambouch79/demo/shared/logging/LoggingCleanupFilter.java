package io.github.sambouch79.demo.shared.logging;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.MDC;

/** Nettoie le MDC après chaque requête pour éviter les fuites de contexte. */
@Provider
public class LoggingCleanupFilter implements ContainerResponseFilter {

  @Override
  public void filter(ContainerRequestContext req, ContainerResponseContext res) {
    MDC.clear();
  }
}

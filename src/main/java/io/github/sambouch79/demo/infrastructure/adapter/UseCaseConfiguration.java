package io.github.sambouch79.demo.infrastructure.adapter;

import io.github.sambouch79.demo.domain.port.in.UserUseCase;
import io.github.sambouch79.demo.domain.port.out.UserPort;
import io.github.sambouch79.demo.domain.usecase.UserUseCaseImpl;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

/**
 * Configuration CDI — instancie les UseCases avec leurs dépendances.
 * Permet au domaine de rester pur (sans annotations CDI).
 */
@ApplicationScoped
public class UseCaseConfiguration {

  @Inject UserPort userPort;

  @Produces
  @ApplicationScoped
  public UserUseCase userUseCase() {
    return new UserUseCaseImpl(userPort);
  }
}

package io.github.sambouch79.demo.shared.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Implémentation de {@link ValidNumeroFiness}.
 * Accepte 9 chiffres (numéro d'établissement) ou 14 chiffres (entité juridique).
 * Les espaces et tirets sont ignorés avant la validation.
 */
public class FinessValidator implements ConstraintValidator<ValidNumeroFiness, String> {

  private boolean optional;

  @Override
  public void initialize(ValidNumeroFiness annotation) {
    this.optional = annotation.optional();
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null) return optional;

    String cleaned = value.trim().replaceAll("[\\s\\-]", "");
    if (cleaned.isEmpty())          return false;
    if (!cleaned.matches("\\d+"))   return false;

    int len = cleaned.length();
    return len == 9 || len == 14;
  }
}

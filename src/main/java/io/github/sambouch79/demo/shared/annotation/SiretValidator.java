package io.github.sambouch79.demo.shared.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Implémentation de {@link ValidSiret}.
 * Vérifie le format (14 chiffres) puis la clé de contrôle par l'algorithme de Luhn.
 */
public class SiretValidator implements ConstraintValidator<ValidSiret, String> {

  private boolean optional;

  @Override
  public void initialize(ValidSiret annotation) {
    this.optional = annotation.optional();
  }

  @Override
  public boolean isValid(String siret, ConstraintValidatorContext context) {
    if (siret == null)        return optional;
    if (siret.trim().isEmpty()) return false;
    if (!siret.matches("\\d{14}")) return false;
    return luhn(siret);
  }

  /**
   * Algorithme de Luhn appliqué au SIRET.
   * Multiplie par 2 les chiffres de rang pair (en partant de la gauche, index 0),
   * soustrait 9 si le produit dépasse 9, puis vérifie que la somme est divisible par 10.
   */
  static boolean luhn(String number) {
    int sum = 0;
    for (int i = 0; i < number.length(); i++) {
      int digit = Character.getNumericValue(number.charAt(i));
      if (i % 2 == 0) {
        digit *= 2;
        if (digit > 9) digit -= 9;
      }
      sum += digit;
    }
    return sum % 10 == 0;
  }
}

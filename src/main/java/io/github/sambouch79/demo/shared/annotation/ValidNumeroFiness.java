package io.github.sambouch79.demo.shared.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

/**
 * Valide un numéro FINESS (France) : 9 chiffres (établissement) ou 14 chiffres (entité juridique).
 * Espaces et tirets sont tolérés et nettoyés avant validation.
 *
 * <pre>{@code
 * @ValidNumeroFiness
 * private String numeroFiness;
 *
 * @ValidNumeroFiness(optional = true)
 * private String finessOptional;
 * }</pre>
 */
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FinessValidator.class)
@Documented
public @interface ValidNumeroFiness {

  String message() default "Le numéro FINESS doit être composé de 9 ou 14 chiffres";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  /** Si {@code true}, une valeur {@code null} est acceptée. */
  boolean optional() default false;
}

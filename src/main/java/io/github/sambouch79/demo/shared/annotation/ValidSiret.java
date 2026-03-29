package io.github.sambouch79.demo.shared.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

/**
 * Valide qu'un numéro SIRET est conforme : 14 chiffres, clé de contrôle Luhn valide.
 *
 * <pre>{@code
 * @ValidSiret
 * private String siret;
 *
 * // Champ optionnel (null accepté) :
 * @ValidSiret(optional = true)
 * private String siretOptional;
 * }</pre>
 */
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SiretValidator.class)
@Documented
public @interface ValidSiret {

  String message() default "Le SIRET doit contenir 14 chiffres et avoir une clé de contrôle valide";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  /** Si {@code true}, une valeur {@code null} est acceptée. */
  boolean optional() default false;
}

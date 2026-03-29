package io.github.sambouch79.demo.api;

import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeIn;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

/**
 * Configuration OpenAPI/Swagger de l'application démo.
 *
 * <p>Démontre l'intégration de {@code quarkus-health-commons} dans un projet Quarkus réel.
 * Authentification par clé API dans le header {@code X-API-KEY}.
 */
@OpenAPIDefinition(
    info = @Info(
        title       = "quarkus-health-commons — Demo API",
        version     = "1.0.0",
        description = "API de démonstration illustrant l'utilisation de quarkus-health-commons : "
            + "@MeasuredEndpoint (métriques Prometheus), @ValidSiret, @ValidNumeroFiness, PageRequest/PageResponse.",
        contact = @Contact(
            name  = "samira-dev",
            url   = "https://github.com/samira-dev/quarkus-health-commons"),
        license = @License(
            name = "Apache 2.0",
            url  = "https://www.apache.org/licenses/LICENSE-2.0")),
    security = @SecurityRequirement(name = "apiKey"),
    tags = {
        @Tag(name = "Users",   description = "Gestion des utilisateurs"),
        @Tag(name = "System",  description = "Endpoints système (ping, version)")
    })
@SecurityScheme(
    securitySchemeName = "apiKey",
    type               = SecuritySchemeType.APIKEY,
    in                 = SecuritySchemeIn.HEADER,
    apiKeyName         = "X-API-KEY",
    description        = "Clé API à transmettre dans le header X-API-KEY. "
        + "En mode démo : valeur = 'demo-secret-key'")
public class OpenAPIConfig extends Application {}

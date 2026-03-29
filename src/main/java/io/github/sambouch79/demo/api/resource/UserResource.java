package io.github.sambouch79.demo.api.resource;

import io.github.sambouch79.demo.api.dto.UserDTO;
import io.github.sambouch79.demo.domain.model.User;
import io.github.sambouch79.demo.domain.port.in.UserUseCase;
import io.github.sambouch79.demo.shared.util.ApiResponsesDefault;
import io.github.sambouch79.demo.api.dto.UserPageRequest;
import io.github.sambouch79.demo.api.mapper.UserMapper;
import io.github.sambouch79.utilscommons.metrics.MeasuredEndpoint;
import io.github.sambouch79.utilscommons.pagination.PageResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.groups.ConvertGroup;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

/**
 * Ressource REST pour la gestion des utilisateurs.
 *
 * <ul>
 *   <li>GET  /api/v1/users            — liste paginée</li>
 *   <li>GET  /api/v1/users/uuid/{uuid} — recherche par UUID</li>
 *   <li>POST /api/v1/users            — création</li>
 *   <li>PATCH /api/v1/users/uuid/{uuid} — mise à jour partielle</li>
 *   <li>DELETE /api/v1/users/uuid/{uuid} — suppression</li>
 * </ul>
 *
 * <p>Démontre l'utilisation de {@code quarkus-utils-commons} :
 * {@link MeasuredEndpoint}
 */
@Slf4j
@Path("/api/v1/users")
@ApiResponsesDefault
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Users", description = "Gestion des utilisateurs")
public class UserResource {

  private final UserUseCase userUseCase;
  private final UserMapper  userMapper;

  @Inject
  public UserResource(UserUseCase userUseCase, UserMapper userMapper) {
    this.userUseCase = userUseCase;
    this.userMapper  = userMapper;
  }

  // ===== GET /api/v1/users =====

  /**
   * Retourne la liste paginée des utilisateurs.
   *
   * @param request paramètres de pagination et de tri
   * @return {@code 200 OK} avec un {@link PageResponse} de {@link UserDTO}
   */
  @GET
  @MeasuredEndpoint(name = "user.list", endpoint = "listAll")
  @Operation(summary = "Lister tous les utilisateurs",
      description = "Retourne une page d'utilisateurs avec support du tri et de la pagination.")
  @APIResponse(responseCode = "200", description = "Liste paginée récupérée avec succès",
      content = @Content(mediaType = MediaType.APPLICATION_JSON,
          schema = @Schema(implementation = PageResponse.class)))
  public Response listAll(@BeanParam @Valid UserPageRequest request) {
    log.debug("listAll - page={}, size={}, sort={}, dir={}",
        request.getPage(), request.getSize(), request.getSortBy(), request.getSortDirection());

    int  page      = request.safePage();
    int  size      = request.safeSize();
    long total     = userUseCase.countAll();

    List<UserDTO> content = userUseCase
        .findPage(page, size, request.safeSortField(), request.isAscending())
        .stream()
        .map(userMapper::toDto)
        .toList();

    return Response.ok(new PageResponse<>(content, total, page, size)).build();
  }

  // ===== GET /api/v1/users/uuid/{uuid} =====

  /**
   * Recherche un utilisateur par son UUID.
   *
   * @param uuid identifiant public de l'utilisateur
   * @return {@code 200 OK} avec le {@link UserDTO}, ou {@code 404} si introuvable
   */
  @GET
  @Path("/uuid/{uuid}")
  @MeasuredEndpoint(name = "user.findByUuid", endpoint = "findByUuid")
  @Operation(summary = "Trouver un utilisateur par UUID")
  @APIResponse(responseCode = "200", description = "Utilisateur trouvé",
      content = @Content(mediaType = MediaType.APPLICATION_JSON,
          schema = @Schema(implementation = UserDTO.class)))
  public Response findByUuid(
      @Parameter(description = "UUID de l'utilisateur", required = true,
          example = "11111111-1111-1111-1111-111111111111")
      @PathParam("uuid") UUID uuid) {

    User user = userUseCase.findByUuid(uuid);
    return Response.ok(userMapper.toDto(user)).build();
  }

  // ===== POST /api/v1/users =====

  /**
   * Crée un nouvel utilisateur.
   * Validation selon le groupe {@link UserDTO.Create}.
   *
   * @param dto données de l'utilisateur à créer
   * @return {@code 201 Created} avec le {@link UserDTO} créé
   */
  @POST
  @MeasuredEndpoint(name = "user.create", endpoint = "create")
  @Operation(summary = "Créer un utilisateur",
      description = "Crée un utilisateur. Le SIRET est validé par l'algorithme de Luhn.")
  @APIResponse(responseCode = "201", description = "Utilisateur créé avec succès",
      content = @Content(mediaType = MediaType.APPLICATION_JSON,
          schema = @Schema(implementation = UserDTO.class)))
  @Transactional
  public Response create(
      @Valid @ConvertGroup(to = UserDTO.Create.class) UserDTO dto) {

    log.debug("create user nom={}", dto.getNom());
    User user    = userMapper.toModelOnCreate(dto);
    User created = userUseCase.create(user);
    return Response.status(Response.Status.CREATED)
        .entity(userMapper.toDto(created))
        .build();
  }

  // ===== PATCH /api/v1/users/uuid/{uuid} =====

  /**
   * Met à jour partiellement un utilisateur (PATCH).
   * Seuls les champs non-null du corps sont appliqués.
   *
   * @param uuid   UUID de l'utilisateur à modifier
   * @param patchDTO champs à mettre à jour
   * @return {@code 204 No Content}
   */
  @PATCH
  @Path("/uuid/{uuid}")
  @MeasuredEndpoint(name = "user.patch", endpoint = "patch")
  @Operation(summary = "Mettre à jour partiellement un utilisateur",
      description = "Seuls les champs fournis dans le corps sont mis à jour.")
  @APIResponse(responseCode = "204", description = "Utilisateur mis à jour")
  @Transactional
  public Response patch(
      @Parameter(description = "UUID de l'utilisateur", required = true)
      @PathParam("uuid") UUID uuid,
      @Valid @ConvertGroup(to = UserDTO.Update.class) UserDTO patchDTO) {

    log.debug("patch user uuid={}", uuid);
    User existing = userUseCase.findByUuid(uuid);
    User updated  = userMapper.updateModelFromDto(patchDTO, existing);
    userUseCase.updateByUuid(updated);
    return Response.noContent().build();
  }

  // ===== DELETE /api/v1/users/uuid/{uuid} =====

  /**
   * Supprime un utilisateur par son UUID.
   *
   * @param uuid UUID de l'utilisateur à supprimer
   * @return {@code 204 No Content}
   */
  @DELETE
  @Path("/uuid/{uuid}")
  @MeasuredEndpoint(name = "user.delete", endpoint = "delete")
  @Operation(summary = "Supprimer un utilisateur")
  @APIResponse(responseCode = "204", description = "Utilisateur supprimé")
  @Transactional
  public Response delete(
      @Parameter(description = "UUID de l'utilisateur", required = true)
      @PathParam("uuid") UUID uuid) {

    userUseCase.deleteByUuid(uuid);
    return Response.noContent().build();
  }
}

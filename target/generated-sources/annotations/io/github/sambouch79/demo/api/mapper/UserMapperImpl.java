package io.github.sambouch79.demo.api.mapper;

import io.github.sambouch79.demo.domain.model.StatutJuridiqueUser;
import io.github.sambouch79.demo.domain.model.StatutUser;
import io.github.sambouch79.demo.api.dto.UserDTO;
import io.github.sambouch79.demo.domain.model.User;
import jakarta.enterprise.context.ApplicationScoped;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-29T19:16:22+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.10 (Oracle Corporation)"
)
@ApplicationScoped
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDTO toDto(User model) {
        if ( model == null ) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setId( model.id() );
        userDTO.setUuid( model.uuid() );
        userDTO.setNom( model.nom() );
        userDTO.setPrenom( model.prenom() );
        userDTO.setSiret( model.siret() );
        userDTO.setNumeroFiness( model.numeroFiness() );
        userDTO.setAffiliationCollectifAssociatif( model.affiliationCollectifAssociatif() );
        userDTO.setOutilTelegestion( model.outilTelegestion() );
        userDTO.setCreePar( model.creePar() );
        userDTO.setModifiePar( model.modifiePar() );
        userDTO.setDateCreation( model.dateCreation() );
        userDTO.setDateModification( model.dateModification() );

        userDTO.setStatut( model.statut() != null ? model.statut().name() : null );
        userDTO.setStatutJuridique( model.statutJuridique() != null ? model.statutJuridique().name() : null );

        return userDTO;
    }

    @Override
    public User toModelOnCreate(UserDTO dto) {
        if ( dto == null ) {
            return null;
        }

        User.Builder user = User.builder();

        user.nom( dto.getNom() );
        user.prenom( dto.getPrenom() );
        user.siret( dto.getSiret() );
        user.numeroFiness( dto.getNumeroFiness() );
        user.affiliationCollectifAssociatif( dto.getAffiliationCollectifAssociatif() );
        user.outilTelegestion( dto.getOutilTelegestion() );
        user.creePar( dto.getCreePar() );

        user.statut( dto.getStatut() != null ? StatutUser.valueOf(dto.getStatut()) : StatutUser.ACTIF );
        user.statutJuridique( StatutJuridiqueUser.valueOf(dto.getStatutJuridique()) );

        return user.build();
    }
}

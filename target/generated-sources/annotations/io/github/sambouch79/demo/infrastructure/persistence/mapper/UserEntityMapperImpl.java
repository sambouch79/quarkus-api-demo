package io.github.sambouch79.demo.infrastructure.persistence.mapper;

import io.github.sambouch79.demo.domain.model.User;
import io.github.sambouch79.demo.infrastructure.persistence.entity.UserEntity;
import jakarta.enterprise.context.ApplicationScoped;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-29T19:16:22+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.10 (Oracle Corporation)"
)
@ApplicationScoped
public class UserEntityMapperImpl implements UserEntityMapper {

    @Override
    public User toDomain(UserEntity entity) {
        if ( entity == null ) {
            return null;
        }

        User.Builder user = User.builder();

        user.statut( toStatutDomain( entity.getStatut() ) );
        user.statutJuridique( toStatutJuridiqueDomain( entity.getStatutJuridique() ) );
        user.id( entity.getId() );
        user.uuid( entity.getUuid() );
        user.nom( entity.getNom() );
        user.prenom( entity.getPrenom() );
        user.siret( entity.getSiret() );
        user.numeroFiness( entity.getNumeroFiness() );
        user.affiliationCollectifAssociatif( entity.getAffiliationCollectifAssociatif() );
        user.outilTelegestion( entity.getOutilTelegestion() );
        user.creePar( entity.getCreePar() );
        user.modifiePar( entity.getModifiePar() );
        user.dateCreation( entity.getDateCreation() );
        user.dateModification( entity.getDateModification() );

        return user.build();
    }

    @Override
    public UserEntity toEntity(User domain) {
        if ( domain == null ) {
            return null;
        }

        UserEntity userEntity = new UserEntity();

        userEntity.setStatut( toStatutEntity( domain.statut() ) );
        userEntity.setStatutJuridique( toStatutJuridiqueEntity( domain.statutJuridique() ) );
        userEntity.setId( domain.id() );
        userEntity.setUuid( domain.uuid() );
        userEntity.setNom( domain.nom() );
        userEntity.setPrenom( domain.prenom() );
        userEntity.setSiret( domain.siret() );
        userEntity.setNumeroFiness( domain.numeroFiness() );
        userEntity.setAffiliationCollectifAssociatif( domain.affiliationCollectifAssociatif() );
        userEntity.setOutilTelegestion( domain.outilTelegestion() );
        userEntity.setCreePar( domain.creePar() );
        userEntity.setModifiePar( domain.modifiePar() );
        userEntity.setDateCreation( domain.dateCreation() );
        userEntity.setDateModification( domain.dateModification() );

        return userEntity;
    }
}

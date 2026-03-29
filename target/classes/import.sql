INSERT INTO users (id, uuid, nom, prenom, siret, numero_finess, statut, statut_juridique, affiliation_collectif_associatif, outil_telegestion, cree_par, date_creation)
VALUES (1, '11111111-1111-1111-1111-111111111111', 'Dupont', 'Marie', '35600000048004', '123456789', 'ACTIF', 'SARL', true, false, 'DEMO_APP', CURRENT_TIMESTAMP);

INSERT INTO users (id, uuid, nom, prenom, siret, numero_finess, statut, statut_juridique, affiliation_collectif_associatif, outil_telegestion, cree_par, date_creation)
VALUES (2, '22222222-2222-2222-2222-222222222222', 'Martin', 'Jean', '73282932000074', null, 'ACTIF', 'SAS', false, true, 'DEMO_APP', CURRENT_TIMESTAMP);

INSERT INTO users (id, uuid, nom, prenom, siret, numero_finess, statut, statut_juridique, affiliation_collectif_associatif, outil_telegestion, cree_par, date_creation)
VALUES (3, '33333333-3333-3333-3333-333333333333', 'Bernard', 'Sophie', '81143020300034', '987654321', 'INACTIF', 'EURL', false, false, 'DEMO_APP', CURRENT_TIMESTAMP);

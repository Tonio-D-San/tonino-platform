CREATE USER keycloak_user WITH PASSWORD 'Veloci-Raptor-Caccia-2026!';
CREATE DATABASE keycloak_db WITH OWNER keycloak_user;

CREATE USER people_service_user WITH PASSWORD 'Triceratopo-3-Corna-Verde*';
CREATE DATABASE people_service WITH OWNER people_service_user;

CREATE USER notification_service_user WITH PASSWORD 'Brachiosauro_Mangia_Foglie!7';
CREATE DATABASE notification_service WITH OWNER notification_service_user;

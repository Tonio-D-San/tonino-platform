CREATE USER kc_db_user WITH PASSWORD 'kc_db_psw';
CREATE DATABASE kc_db WITH OWNER kc_db_user;

CREATE USER identity_user WITH PASSWORD 'identity_psw';
CREATE DATABASE identity_db WITH OWNER identity_user;

CREATE USER ntf_db_user WITH PASSWORD 'ntf_db_psw';
CREATE DATABASE ntf_db WITH OWNER ntf_db_user;

--- DEV ---
CREATE USER kc_db_user_dev WITH PASSWORD 'kc_db_psw_dev';
CREATE DATABASE kc_db_dev WITH OWNER kc_db_user_dev;

CREATE USER identity_user_dev WITH PASSWORD 'identity_psw_dev';
CREATE DATABASE identity_db_dev WITH OWNER identity_user_dev;

CREATE USER ntf_db_user_dev WITH PASSWORD 'ntf_db_psw_dev';
CREATE DATABASE ntf_db_dev WITH OWNER ntf_db_user_dev;

--- TEST ---
CREATE USER kc_db_user_test WITH PASSWORD 'kc_db_psw_test';
CREATE DATABASE kc_db_test WITH OWNER kc_db_user_test;

CREATE USER identity_user_test WITH PASSWORD 'identity_psw_test';
CREATE DATABASE identity_db_test WITH OWNER identity_user_test;

CREATE USER ntf_db_user_test WITH PASSWORD 'ntf_db_psw_test';
CREATE DATABASE ntf_db_test WITH OWNER ntf_db_user_test;

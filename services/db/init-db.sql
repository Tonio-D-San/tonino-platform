CREATE USER kc_db_user WITH PASSWORD 'kc_db_psw';
CREATE DATABASE kc_db WITH OWNER kc_db_user;

CREATE USER people_user WITH PASSWORD 'people_psw';
CREATE DATABASE people_db WITH OWNER people_user;

CREATE USER ntf_db_user WITH PASSWORD 'ntf_db_psw';
CREATE DATABASE ntf_db WITH OWNER ntf_db_user;

--- DEV ---
CREATE USER kc_db_user_dev WITH PASSWORD 'kc_db_psw_dev';
CREATE DATABASE kc_db_dev WITH OWNER kc_db_user_dev;

CREATE USER people_user_dev WITH PASSWORD 'people_psw_dev';
CREATE DATABASE people_db_dev WITH OWNER people_user_dev;

CREATE USER ntf_db_user_dev WITH PASSWORD 'ntf_db_psw_dev';
CREATE DATABASE ntf_db_dev WITH OWNER ntf_db_user_dev;

--- TEST ---
CREATE USER kc_db_user_test WITH PASSWORD 'kc_db_psw_test';
CREATE DATABASE kc_db_test WITH OWNER kc_db_user_test;

CREATE USER people_user_test WITH PASSWORD 'people_psw_test';
CREATE DATABASE people_db_test WITH OWNER people_user_test;

CREATE USER ntf_db_user_test WITH PASSWORD 'ntf_db_psw_test';
CREATE DATABASE ntf_db_test WITH OWNER ntf_db_user_test;

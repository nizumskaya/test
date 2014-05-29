
CREATE DATABASE servlet
  WITH OWNER = servlet
       ENCODING = 'UTF8'
       TABLESPACE = pg_default
       LC_COLLATE = 'ru_RU.UTF-8'
       LC_CTYPE = 'ru_RU.UTF-8'
       CONNECTION LIMIT = -1;

       


CREATE ROLE servlet LOGIN
  NOSUPERUSER INHERIT NOCREATEDB NOCREATEROLE NOREPLICATION;
  

  

CREATE TABLE customer
(
  id bigserial NOT NULL,
  customer_login character varying(255) NOT NULL,
  customer_password character varying(255) NOT NULL,
  CONSTRAINT customer_pkey PRIMARY KEY (id),
  CONSTRAINT customer_login_unique UNIQUE (customer_login)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE customer
  OWNER TO servlet;

  
  

CREATE TABLE balance
(
  id bigserial NOT NULL,
  customer_id bigint NOT NULL,
  customer_balance double precision NOT NULL,
  CONSTRAINT balance_pkey PRIMARY KEY (id),
  CONSTRAINT balance_customer_id_fkey FOREIGN KEY (customer_id)
      REFERENCES customer (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT balance_customer_id_unique UNIQUE (customer_id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE balance
  OWNER TO servlet;

  
  
insert into customer (customer_login,customer_password)
values ('1234567890', 'password');

INSERT INTO balance (customer_id,customer_balance)
VALUES (1, 55.66);


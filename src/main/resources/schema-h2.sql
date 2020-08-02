create schema if not exists CAMEL_OWNER;
set schema CAMEL_OWNER;

drop table if exists CAMEL_REGISTRY;

create table CAMEL_REGISTRY (
    ID           NUMBER(10)  NOT NULL,
    BIRTH_YEAR   NUMBER( 4)  NOT NULL,
    BIRTH_MONTH  NUMBER( 4)  NOT NULL,
    HUMPS        NUMBER( 1)  NOT NULL,
    WEIGHT       NUMBER( 6)  NOT NULL
);
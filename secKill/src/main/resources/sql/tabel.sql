-- auto-generated definition
create table gift_order
(
    id          int auto_increment                    primary key,
    order_id    varchar(255)                          not null,
    user_id     bigint                                not null,
    sku_code    varchar(50)                           not null,
    status      varchar(20) default 'PROCESSING'      not null,
    create_time datetime    default CURRENT_TIMESTAMP null
);

-- auto-generated definition
create table product
(
    id          bigint auto_increment primary key,
    sku_code    varchar(50) not null,
    stock       int         not null,
    create_time datetime    null,
    constraint sku_code
        unique (sku_code)
);

INSERT INTO cnblog.product (id, sku_code, stock, create_time) VALUES (1, 's001', 1000, '2025-03-26 16:11:21.0');
INSERT INTO cnblog.product (id, sku_code, stock, create_time) VALUES (2, 's002', 1000, '2025-03-26 16:11:41.0');
INSERT INTO cnblog.product (id, sku_code, stock, create_time) VALUES (3, 's003', 1000, '2025-03-26 16:11:58.0');

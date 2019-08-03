create table review
(
    id int auto_increment primary key,
    title varchar(255) not null,
    text varchar(500) null,
    product_id  int not null
);

create table product
(
    id int auto_increment primary key,
    name varchar(255) not null,
    description varchar(100) not null
);

create table comment
(
    id int auto_increment primary key,
    title varchar(255) not null,
    text varchar(10000) null,
    review_id int not null
);
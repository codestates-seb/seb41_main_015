create table if not exists book
(
    book_id     bigint auto_increment
        primary key,
    created_at  datetime     null,
    modified_at datetime     null,
    author      varchar(255) null,
    avg_rate    double       null,
    book_title  varchar(255) null,
    isbn        varchar(50)  not null,
    publisher   varchar(255) null,
    rate_count  bigint       null,
    thumbnail   longtext     null,
    total_rate  bigint       null,
    constraint UK_ehpdfjpu1jm3hijhj4mm0hx9h
        unique (isbn)
);

create table if not exists member
(
    member_id     bigint auto_increment
        primary key,
    created_at    datetime                null,
    modified_at   datetime                null,
    address       varchar(255) default '' null,
    display_name  varchar(20)             null,
    email         varchar(100)            not null,
    img_url       longtext                null,
    member_status varchar(20)             not null,
    name          varchar(30)             null,
    phone_number  varchar(30)             null,
    constraint UK_k3kf09s6pp3ntqfis89la08gq
        unique (display_name),
    constraint UK_mbmcqelty0fbrvxp1q58dn57t
        unique (email)
);

create table if not exists borrow
(
    borrow_id    bigint auto_increment
        primary key,
    created_at   datetime     null,
    modified_at  datetime     null,
    author       varchar(50)  null,
    book_title   varchar(100) null,
    borrow_whthr bit          null,
    content      longtext     null,
    display_name varchar(50)  null,
    publisher    varchar(50)  null,
    talk_url     varchar(100) null,
    thumbnail    longtext     null,
    title        varchar(100) null,
    view         bigint       null,
    member_id    bigint       null,
    constraint FKjwuo6jvfpmjevtx72804p4cp5
        foreign key (member_id) references member (member_id)
);

create table if not exists borrow_comment
(
    borrow_comment_id bigint auto_increment
        primary key,
    created_at        datetime    null,
    modified_at       datetime    null,
    content           longtext    not null,
    display_name      varchar(50) null,
    borrow_id         bigint      null,
    member_id         bigint      null,
    constraint FK9gtriq9ftivo717w6x2f5j2ml
        foreign key (member_id) references member (member_id),
    constraint FKngbtwys0jqjyjjtorgkwh0kj5
        foreign key (borrow_id) references borrow (borrow_id)
);

create table if not exists community
(
    community_id bigint auto_increment
        primary key,
    created_at   datetime     null,
    modified_at  datetime     null,
    content      longtext     not null,
    display_name varchar(100) null,
    title        varchar(100) not null,
    type         varchar(255) not null,
    view         bigint       null,
    member_id    bigint       null,
    constraint FKhcwt9ggjfvanatb6u621vbf1r
        foreign key (member_id) references member (member_id)
);

create table if not exists community_comment
(
    community_comment_id bigint auto_increment
        primary key,
    created_at           datetime     null,
    modified_at          datetime     null,
    content              longtext     not null,
    display_name         varchar(100) null,
    community_id         bigint       null,
    member_id            bigint       null,
    constraint FK5s7mlxnl3exdg2iy49qjpjsp7
        foreign key (community_id) references community (community_id),
    constraint FKgvpkqkaisk26mt8qpnw1tyfav
        foreign key (member_id) references member (member_id)
);

create table if not exists member_roles
(
    member_member_id bigint       not null,
    roles            varchar(255) null,
    constraint FKruptm2dtwl95mfks4bnhv828k
        foreign key (member_member_id) references member (member_id)
);

create table if not exists rate
(
    rate_id      bigint auto_increment
        primary key,
    created_at   datetime     null,
    modified_at  datetime     null,
    content      longtext     not null,
    display_name varchar(255) null,
    rating       bigint       null,
    book_id      bigint       null,
    member_id    bigint       null,
    constraint FKa4dg2b5yjqy31vndopc14h6vl
        foreign key (book_id) references book (book_id),
    constraint FKcfm2uxk2pb1t2629s91p1ksu9
        foreign key (member_id) references member (member_id)
);

create table if not exists refresh_token
(
    refresh_token_id bigint auto_increment
        primary key,
    refresh_token    varchar(255) null,
    member           bigint       null,
    constraint FKn3m5lap6p90vcnwhkk9tfeilk
        foreign key (member) references member (member_id)
);

create table if not exists request
(
    request_id   bigint auto_increment
        primary key,
    created_at   datetime     null,
    modified_at  datetime     null,
    author       varchar(100) null,
    book_title   varchar(100) null,
    content      longtext     null,
    display_name varchar(100) null,
    publisher    varchar(100) null,
    talk_url     varchar(100) null,
    thumbnail    longtext     null,
    title        varchar(100) null,
    view         bigint       null,
    member_id    bigint       null,
    constraint FKlgy36p5youj5oe1irwbit23ct
        foreign key (member_id) references member (member_id)
);

create table if not exists request_comment
(
    request_comment_id bigint auto_increment
        primary key,
    created_at         datetime     null,
    modified_at        datetime     null,
    content            longtext     not null,
    display_name       varchar(100) null,
    member_id          bigint       null,
    request_id         bigint       null,
    constraint FKnn3ebd8kkk79xl8sa9as7jg14
        foreign key (request_id) references request (request_id),
    constraint FKqrl57321yv4onx5dybo84fjhh
        foreign key (member_id) references member (member_id)
);

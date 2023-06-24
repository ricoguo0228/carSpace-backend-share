# 建议MySQL版本大于5.6，否则无法使用CURRENT_TIMESTAMP作为默认值
create table user
(
    user_id       bigint auto_increment comment '用户id'
        primary key,
    user_account  varchar(256)                                                                                               not null comment '用户名',
    user_password varchar(128)                                                                                               not null comment '用户密码',
    user_phone    varchar(16)                                                                                                not null comment '用户手机号',
    user_role     tinyint                      default 0                                                                     null comment '用户身份 0-普通用户 1-管理员',
    user_credit   int                          default 100                                                                   null comment '用户信用-百分制',
    is_delete     tinyint                      default 0                                                                     null comment '逻辑删除 0-未删除 1-已删除',
    insert_time   datetime                     default CURRENT_TIMESTAMP                                                     null comment '插入时间',
    update_time   datetime                     default CURRENT_TIMESTAMP                                                     null on update CURRENT_TIMESTAMP comment '信息更新时间',
    nick_name     varchar(16) charset utf8mb3  default '车享用户'                                                            null comment '昵称',
    user_avatar   varchar(512) charset utf8mb3 default 'https://gw.alipayobjects.com/zos/rmsportal/KDpgvguMpGfqaHPjicRK.svg' null comment '用户头像'
)
    comment '用户' engine = InnoDB;

create table carspace
(
    car_id       bigint auto_increment comment '车位id'
        primary key,
    location     varchar(128) charset utf8mb3       not null comment '车位地点',
    price        int                                null comment '车位出价（元/时）',
    image_url    varchar(512) charset utf8mb3       null comment '车位图片地址',
    car_status   tinyint  default 0                 null comment '车辆状态 0-正常未发布 1-已经发布无人预定 2-已经被预定',
    insert_time  datetime default CURRENT_TIMESTAMP null comment '车位创建时间',
    is_delete    tinyint  default 0                 null comment '逻辑删除 0-未删除 1-已删除',
    update_time  datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '车位更新时间',
    owner_id     bigint                             not null comment '车位拥有者',
    count_time   int      default 0                 null comment '被点击次数',
    reserve_time int      default 0                 null comment '被预约的次数',
    constraint carspace_user_user_id_fk
        foreign key (owner_id) references user (user_id)
)
    comment '用户创建的车位表' engine = InnoDB;

create table ireserve
(
    car_id      bigint                             not null comment '车位id',
    start_time  datetime                           not null comment '车位可预约的开始时间',
    end_time    datetime                           not null comment '车位可预约的结束时间',
    insert_time datetime default CURRENT_TIMESTAMP null comment '数据插入时间',
    is_delete   tinyint  default 0                 null comment '是否删除 0-删除 1-未删除',
    update_time datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '数据更新时间',
    i_id        bigint auto_increment comment '唯一标识'
        primary key,
    constraint ireserve_carspace_car_id_fk
        foreign key (car_id) references carspace (car_id)
)
    engine = InnoDB;

create table reservation
(
    reserve_id         bigint auto_increment comment '预约id'
        primary key,
    reserver_id        bigint                             not null comment '预约者id',
    car_id             bigint                             not null comment '被预约车位的id',
    reserve_status     tinyint  default 0                 null comment '预约状态 0-正在预约 1-预约完成',
    reserve_start_time datetime                           not null comment '预约开始时间',
    reserve_end_time   datetime                           not null comment '预约结束时间',
    insert_time        datetime default CURRENT_TIMESTAMP null comment '数据插入时间',
    update_time        datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '数据更新时间',
    car_pass           varchar(16) charset utf8mb3        null comment '车牌',
    constraint Reservation_carspace_car_id_fk
        foreign key (car_id) references carspace (car_id),
    constraint Reservation_user_user_id_fk
        foreign key (reserver_id) references user (user_id)
)
    comment '预约表' engine = InnoDB;

create definer = root@localhost event delete_old_records on schedule
    every '1' SECOND
        starts '2023-06-21 15:59:25'
    enable
    do
UPDATE ireserve
SET is_delete = 1
WHERE end_time < CURRENT_TIMESTAMP;


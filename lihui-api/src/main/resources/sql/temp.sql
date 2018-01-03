
select * from user;

insert into user values(
1000000001
,'lh'
,'today36524'
,'13344445566'
,'abc@123.com'
,'123456789'
,0
,sysdate()
,2000000001
,CURRENT_TIMESTAMP
,2000000001
,'');


insert into user
(
user_name
,password
,telephone
,email
,qq
,integral
,created_at
,created_by
,updated_at
,updated_by
,remark
) values
(
'lh'
,'today36524'
,'13344445566'
,'abc@123.com'
,'123456789'
,0
,CURRENT_TIMESTAMP
,2000000001
,CURRENT_TIMESTAMP
,2000000001
,''
);

insert into integral_journal
(
   user_id,
     integral_type,
     integral_price,
     integral_source,
     integral,
     created_at,
     created_by,
     updated_at,
     updated_by,
     remark
)
select
   id as user_id
   ,1 as integral_type
   ,0 as integral_price
   ,1 as integral_source
   ,0 as integral
   ,CURRENT_TIMESTAMP as created_at
    ,2000000001 as created_by
    ,CURRENT_TIMESTAMP as updated_at
    ,2000000001 as updated_by
    ,'' as remark
from user where telephone='13345678901';

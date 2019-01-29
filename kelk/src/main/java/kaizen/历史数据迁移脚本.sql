


--数据范围  

--insert into t_data_range
--select nextval('t_data_range_id_seq') as id,null,null,null,null
--,t.range as polygon, md5(ST_AsText(t.range))  
--from (select distinct range from task_business_info where range is not null) as t;

insert into t_data_range
select 
nextval('t_data_range_id_seq') as id
,null
,st_x(st_centroid(ST_GeomFromText(range,4326)))
,st_y(st_centroid(ST_GeomFromText(range,4326)))
,(
  select tbi.bounds from task_business_info tbi ,act_hi_procinst ahp
  where tbi.range=ST_GeomFromText(t.range,4326) 
  and tbi.range is not null 
  and tbi.id=ahp.business_key_::bigint 
  and ahp.delete_reason_ is null
  limit 1
)
,ST_GeomFromText(range,4326)
, md5(t.range)  
from 
(
  select distinct ST_AsText(tbi.range) as range from task_business_info tbi,act_hi_procinst ahp 
  where tbi.range is not null
  and tbi.id=ahp.business_key_::bigint
  and ahp.delete_reason_ is null 
 ) as t;



--数据轨迹
insert into t_data_track
select nextval('t_data_track_id_seq') as id,track_id,track_name as name,null,null,the_geom as line from track_shape;


--任务表
insert into task_business
select nextval('task_business_id_seq') as id,
tbi.type,
tbi.name,
tbi.state,
tbi.create_by,
tbi.create_at,
tbi.update_by,
tbi.update_at,
0 
from 
task_business_info  tbi,act_hi_procinst ahp 
where 
tbi.id=ahp.business_key_::bigint
and ahp.delete_reason_ is null ;

--tag表
insert into task_business_tag
select distinct (s.task_id::bigint)  as task_id,h.name_ as k,h.text_ as v 
from task_service_context  s,
act_hi_varinst h,    
act_hi_procinst ahp
where   s.task_id !='null'
and  h.proc_inst_id_= s.p_process_instance_id 
and s.task_id=ahp.business_key_  
and ahp.delete_reason_ is null; 
       
--任务批次表
insert into task_batch
select distinct s.sr_task_id::bigint,s.sr_task_batch  ,split_part(s.sr_task_batch ,'_', 2)::int as seq 
from 
task_service_context  s ,
act_hi_procinst      ahp
where s.task_id != 'null' 
and split_part(s.sr_task_batch ,'_', 2) !='' 
and s.task_id=ahp.business_key_  
and ahp.delete_reason_ is null 
order by seq desc;




--数据处理（这个数据处理出现了问题）
ALTER TABLE t_data_process ADD tsc_id bigint;  
ALTER TABLE t_data_process ADD data_code character varying(250);  


with temp as(
select distinct
c.task_id::bigint
,t.id buss_type
,(select e.id from t_scene e where e.scene_code= v.text_) scene_type
--,s_process_instance_id
,(select act_name_ from act_hi_actinst a where s_process_instance_id=a.call_proc_inst_id_ order by end_time_ desc limit 1) link
, c.id
,data_type
from task_service_context c,t_buss_type t,act_hi_procinst p
left join act_hi_varinst v on v.proc_inst_id_=p.proc_inst_id_ and v.name_='sceneCode' 
where c.context_type='0'
and p.business_key_= c.task_id
--and task_id='10733'
--and task_id='7658'
and c.buss_code in ('survey','auto','recog','fusion')
and c.buss_code=t.buss_code
and p.delete_reason_ is null
--and c.sr_task_seq !=''
--order by task_id desc 
)

insert into t_data_process

select nextval('t_data_process_id_seq') as id,* from temp;


--数据处理服务  
ALTER TABLE t_data_process_serv ADD tsc_id bigint;  

with tt as (
 SELECT t.sr_task_seq AS task_seq
        , 2 AS status
        , t.service_code AS service_app
	, t.service_version AS service_version
	, t.service_instance AS service_instance
	, t.service_type AS service_type
	, t.service_params AS service_params
	, t.service_result AS service_result
	, t.s_process_instance_id AS s_pid
	, 'startService' AS s_aid
	, extract(epoch FROM a.start_time_) * 1000 AS start_at
	, extract(epoch FROM a.end_time_) * 1000 AS end_at
	, 0
	,t.id tsc_id
FROM 
task_service_context t
left join 
act_hi_actinst a 
on t.s_process_instance_id=a.proc_inst_id_
and a.act_id_='triggerEndService'

left join act_hi_procinst ahp
on t.task_id=ahp.business_key_  

where 
t.sr_task_seq !=''
and t.context_type='0'
and t.buss_code in ('survey','auto','recog','fusion')
and ahp.delete_reason_ is null 
order by end_at desc
),

mm as (
select nextval('t_data_process_serv_id_seq'::regclass) id, * from tt
),

yy as (
select min(id) mid from mm group by task_seq
)
 
insert into t_data_process_serv  
select * from mm where id in(select mid from yy);


 
--数据表
ALTER TABLE t_data ADD task_id bigint;  
ALTER TABLE t_data ADD proc_ser_id bigint;  
   

insert into t_data
select nextval('t_data_id_seq') as id
,( select r.id from t_data_range r ,task_business_info t where split_part(task_seq ,'_', 1)::bigint=t.id  and md5(ST_AsText(t.range))=r.md5) as data_range_id
,0 as deleted
,1 as as_source
,1 as status
,split_part(task_seq ,'_', 1)::bigint as task_id
,s.id as proc_ser_id
from 
t_data_process_serv s;



--输出数据关系    

with temp as (
select sr_task_id::bigint as sr_task_id,max(sr_task_batch) as sr_task_batch 
from task_service_context c,
act_hi_procinst ahp
where 
c.task_id=ahp.business_key_  
and ahp.delete_reason_ is null 
group by sr_task_id
)

insert into t_data_output
select 
p.id as pro_id
, (select d.id from t_data d where d.proc_ser_id=s.id) as data_id
,s.id as proc_ser_id
,(
case when (select sr_task_batch from temp c where  p.task_id=c.sr_task_id ) is null 
then ((select p.task_id from t_data_process p where p.tsc_id=s.tsc_id)||'_1')
 else (select sr_task_batch from temp c where p.task_id=c.sr_task_id  ) 
 end)  
,1 as passed,0 as deleted
from 
t_data_process_serv s 
left join t_data_process p
on  p.tsc_id=s.tsc_id;



--轨迹关系表   
insert into t_data_track_relation
select d.id,c.id
from 
t_data_process_serv s,
task_track t,
t_data d,
t_data_track c
where 
split_part(s.task_seq ,'_', 1)=t.task_id
and t.task_id::bigint=d.task_id
and t.track_id=c.track_id;




--数据类型关系表
insert into t_data_type_relation
select d.id,t.id  
from 
t_data d,
t_data_output o,
t_data_process p,
t_data_type t
where 
d.id=o.data_id
and o.proc_id=p.id
and p.data_code=t.data_type_code;


--来源数据
ALTER TABLE t_source_data ADD proc_id bigint;  
--采集-识别
with temp as(
select distinct 
     p.business_key_ t_task_id,
     tp.id t_proc_id
     ,v.text_ s_task_ids
     ,unnest(string_to_array(v.text_ ,',')) s_task_id
      from act_hi_procinst p
           ,act_hi_varinst v,
           t_data_process tp
 where p.proc_inst_id_=v.proc_inst_id_ 
      and v.name_='sourceTaskIds'
      and super_process_instance_id_ is null
      and tp.task_id=p.business_key_::bigint
      and p.delete_reason_ is null 
      and tp.buss_type=2
      --and business_key_='10781'
)

insert into t_source_data
select 
nextval('t_source_data_id_seq') as id,
t_task_id::bigint task_id
,o.data_id data_id
,d.data_range_id data_range_id
--t.*
--,sp.id s_proc_id
,t.t_proc_id proc_id
from temp t, 
t_data_process sp,
t_data_output o,
t_data d 
where 
sp.task_id =s_task_id::bigint
and sp.buss_type=1
and sp.id=o.proc_id
and o.data_id=d.id;



--自动化-融合
with temp as(
select distinct 
     p.business_key_ t_task_id,
     tp.id t_proc_id
     ,v.text_ s_task_ids
     ,unnest(string_to_array(v.text_ ,',')) s_task_id
      from act_hi_procinst p
           ,act_hi_varinst v,
           t_data_process tp
 where p.proc_inst_id_=v.proc_inst_id_ 
      and v.name_='sourceTaskIds'
      and super_process_instance_id_ is null
      and tp.task_id=p.business_key_::bigint
      and p.delete_reason_ is null 
      and tp.buss_type=4
      --and business_key_='10781'
)

insert into t_source_data
select 
nextval('t_source_data_id_seq') as id,
t_task_id::bigint task_id
,o.data_id data_id
,d.data_range_id data_range_id
--t.*
--,sp.id s_proc_id
,t.t_proc_id proc_id
from temp t, 
t_data_process sp,
t_data_output o,
t_data d 
where 
sp.task_id =s_task_id::bigint
and sp.buss_type=3
and sp.id=o.proc_id
and o.data_id=d.id;




--自动化-融合
with temp as(
select distinct 
     p.business_key_ t_task_id,
     tp.id t_proc_id
     ,v.text_ s_task_ids
     ,unnest(string_to_array(v.text_ ,',')) s_task_id
      from act_hi_procinst p
           ,act_hi_varinst v,
           t_data_process tp
 where p.proc_inst_id_=v.proc_inst_id_ 
      and v.name_='sourceTaskIds'
      and super_process_instance_id_ is null
      and tp.task_id=p.business_key_::bigint
      and p.delete_reason_ is null 
      and tp.buss_type=4
      --and business_key_='10781'
)

insert into t_source_data
select 
nextval('t_source_data_id_seq') as id,
t_task_id::bigint task_id
,o.data_id data_id
,d.data_range_id data_range_id
--t.*
--,sp.id s_proc_id
,t.t_proc_id proc_id
from temp t, 
t_data_process sp,
t_data_output o,
t_data d 
where 
sp.task_id =s_task_id::bigint
and sp.buss_type=3
and sp.id=o.proc_id
and o.data_id=d.id;


--来源数据轨迹关系
insert into t_source_data_track_relation
select d.id,tt.id 
from t_source_data d
,task_track t,
t_data_track tt
where 
d.task_id=t.task_id::bigint
and t.track_id=tt.track_id
order by d.id;



--输入数据关系
insert into t_data_input
select 
d.proc_id proc_id
,d.id source_data_id
from t_source_data d
order by proc_id desc;



--来源数据类型关系
insert into t_source_data_type_relation 
select 
sd.id as source_data_id
,r.data_type as data_type 
from 
t_data_process p,
t_data_input i,
t_source_data sd,

t_data_output o,
t_data d,
t_data_type_relation r

where 
   p.id=i.proc_id
and p.id=o.proc_id  
and sd.id=i.source_data_id
and o.data_id=d.id
and r.data_id=d.id;

--数据属性表

insert into t_data_tag
select distinct o.data_id,v.name_,v.text_ from 
task_service_context  c 
left join t_data_process_serv s on c.task_id=split_part(s.task_seq ,'_', 1)
left join t_data_output o on s.id=o.proc_ser_id
inner join act_hi_varinst v on v.proc_inst_id_=c.p_process_instance_id
inner join act_hi_procinst ahp on c.task_id=ahp.business_key_ and ahp.delete_reason_ is null 
where v.name_ in('dbCode','adCode','fusionInstanceId','autoInstanceId','modelEnv','specCode','measure_method','storageType','supportTrans','supportFusion','checkBatch','fusionCheckBatch','fusionErrorCount','editCheckBatch','surveyErrorCount','editCheckErrorCount','surveyCheckBatch')
order by o.data_id asc;


--刪除临时字段
ALTER TABLE t_data_process DROP COLUMN tsc_id; 
ALTER TABLE t_data_process DROP COLUMN data_code; 
ALTER TABLE t_data_process_serv DROP COLUMN tsc_id; 
ALTER TABLE t_data DROP COLUMN task_id; 
ALTER TABLE t_data DROP COLUMN proc_ser_id; 
ALTER TABLE t_source_data DROP COLUMN proc_id; 





--bug修復   数据类型

insert into t_data_type_relation
select m.id,n.id from 
(select distinct  id  from t_data where id not in(select distinct data_id  from t_data_type_relation )) m,
t_data_type n;


insert into t_source_data_type_relation
select m.id,n.id from 
(select distinct  id  from t_data where id not in(select distinct source_data_id  from t_source_data_type_relation )) m,
t_data_type n;
 


--补全t_data_process中 车道线scene_type为空的sql

update t_data_process  set scene_type =(select distinct id from t_scene where scene_code='HD_DATA_LANE') where id in (
	select id from t_data_process where scene_type is null and task_id in (
	   select  distinct business_key_::bigint  from act_hi_procinst where delete_reason_ is null and   proc_inst_id_ in (select  distinct id_ from act_hi_procinst where delete_reason_ is null  and proc_def_id_ in(select distinct id_ from  act_re_procdef where key_='A-Survey_Auto-Rerun_L_RA'))
	)
);

--补全t_data_process中  定位目标scene_type为空的sql

update t_data_process  set scene_type =(select distinct id from t_scene where scene_code='HD_DATA_LO') where id in (
	select id from t_data_process where scene_type is null and task_id in (
	  select  distinct business_key_::bigint from act_hi_procinst where delete_reason_ is null and  proc_inst_id_ in(select distinct id_ from act_hi_procinst where delete_reason_ is null  and proc_def_id_ in (select distinct id_  from  act_re_procdef where key_='A-Survey_Auto-Rerun_LO_RA'))
	)
);




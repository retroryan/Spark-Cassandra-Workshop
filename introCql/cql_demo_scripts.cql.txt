create keyspace sample_ks with replication = {'class':'SimpleStrategy','replication_factor':1};

use sample_ks;

create table messages1 (
body text,
dt text,
ch text,
cu text,
sent text,
primary key (dt)
);

create table messages2 (
body text,
dt text,
ch text,
cu text,
sent text,
primary key ((ch,dt))
);

create table messages3 (
body text,
dt text,
ch text,
cu text,
sent text,
primary key (ch,dt)
);

create table messages4 (
body text,
dt text,
ch text,
cu text,
sent text,
primary key (ch,cu,dt)
);

create table messages5 (
body text,
dt text,
ch text,
cu text,
sent text,
primary key ((ch,dt),cu)
);

begin batch
insert into sample_ks.messages1 (body,dt,ch,cu,sent) values ('I’m feeling sick.','20140402','twitr','1','neg');
insert into sample_ks.messages2 (body,dt,ch,cu,sent) values ('I’m feeling sick.','20140402','twitr','1','neg');
insert into sample_ks.messages3 (body,dt,ch,cu,sent) values ('I’m feeling sick.','20140402','twitr','1','neg');
insert into sample_ks.messages4 (body,dt,ch,cu,sent) values ('I’m feeling sick.','20140402','twitr','1','neg');
insert into sample_ks.messages5 (body,dt,ch,cu,sent) values ('I’m feeling sick.','20140402','twitr','1','neg');
apply batch;

begin batch
insert into sample_ks.messages1 (body,dt,ch,cu,sent) values ('That was sick!','20140401','faceb','1','pos');
insert into sample_ks.messages2 (body,dt,ch,cu,sent) values ('That was sick!','20140401','faceb','1','pos');
insert into sample_ks.messages3 (body,dt,ch,cu,sent) values ('That was sick!','20140401','faceb','1','pos');
insert into sample_ks.messages4 (body,dt,ch,cu,sent) values ('That was sick!','20140401','faceb','1','pos');
insert into sample_ks.messages5 (body,dt,ch,cu,sent) values ('That was sick!','20140401','faceb','1','pos');
apply batch;

begin batch
insert into sample_ks.messages1 (body,dt,ch,cu,sent) values ('I feel sick, too.','20140402','faceb','2','neg');
insert into sample_ks.messages2 (body,dt,ch,cu,sent) values ('I feel sick, too.','20140402','faceb','2','neg');
insert into sample_ks.messages3 (body,dt,ch,cu,sent) values ('I feel sick, too.','20140402','faceb','2','neg');
insert into sample_ks.messages4 (body,dt,ch,cu,sent) values ('I feel sick, too.','20140402','faceb','2','neg');
insert into sample_ks.messages5 (body,dt,ch,cu,sent) values ('I feel sick, too.','20140402','faceb','2','neg');
apply batch;

begin batch
insert into sample_ks.messages1 (body,dt,ch,cu,sent) values ('Dude, you’re sick.','20140403','faceb','1','pos');
insert into sample_ks.messages2 (body,dt,ch,cu,sent) values ('Dude, you’re sick.','20140403','faceb','1','pos');
insert into sample_ks.messages3 (body,dt,ch,cu,sent) values ('Dude, you’re sick.','20140403','faceb','1','pos');
insert into sample_ks.messages4 (body,dt,ch,cu,sent) values ('Dude, you’re sick.','20140403','faceb','1','pos');
insert into sample_ks.messages5 (body,dt,ch,cu,sent) values ('Dude, you’re sick.','20140403','faceb','1','pos');
apply batch;

begin batch
insert into sample_ks.messages1 (body,dt,ch,cu,sent) values ('How sick was that?','20140403','faceb','3','pos');
insert into sample_ks.messages2 (body,dt,ch,cu,sent) values ('How sick was that?','20140403','faceb','3','pos');
insert into sample_ks.messages3 (body,dt,ch,cu,sent) values ('How sick was that?','20140403','faceb','3','pos');
insert into sample_ks.messages4 (body,dt,ch,cu,sent) values ('How sick was that?','20140403','faceb','3','pos');
insert into sample_ks.messages5 (body,dt,ch,cu,sent) values ('How sick was that?','20140403','faceb','3','pos');
apply batch;

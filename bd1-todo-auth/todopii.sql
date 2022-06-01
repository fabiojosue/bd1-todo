-- ---------------------
-- FABIO CALDERON TORRES
-- CARNET 2018156314
-- MYSQL SCRIPT TAREA 3
-- ---------------------
CREATE USER IF NOT EXISTS 'authappuser' IDENTIFIED BY 'authapppassword';
GRANT ALL PRIVILEGES ON todopii.* TO 'authappuser';

SHOW GRANTS FOR 'authappuser';

drop database if exists todopii;

create database todopii;

use todopii;

-- ------------------
-- CREACION DE TABLAS
-- ------------------

CREATE TABLE clients(
	clientId varchar(50) primary key,
    clientSecret varchar(50)
);

CREATE TABLE sessions(
	sessionId varchar(50),
    clientId varchar(50),
    createdat timestamp not null,
    primary key(sessionId, clientId),
    FOREIGN KEY (clientId)
		REFERENCES clients(clientId)
);

-- ------------------
-- INSERCION DE DATOS
-- ------------------
INSERT INTO clients(clientId, clientSecret) VALUES('user1','pass1');
INSERT INTO clients(clientId, clientSecret) VALUES('user2','pass2');
INSERT INTO clients(clientId, clientSecret) VALUES('user3','pass3');
INSERT INTO clients(clientId, clientSecret) VALUES('user4','pass4');

INSERT INTO sessions(sessionId, clientId, createdat) VALUES('session1','user1',current_timestamp());
INSERT INTO sessions(sessionId, clientId, createdat) VALUES('session2','user3',current_timestamp());
INSERT INTO sessions(sessionId, clientId, createdat) VALUES('session3','user5',current_timestamp());

-- ----------------------
-- SELCCIONES / CONSULTAS
-- ----------------------

-- select * from sessions;

-- si el cliente existe
select if(count(*) = 1, true, false) as client_id_exists from clients where clientId = 'user1';
-- si el cliente tiene una sesion
select if(count(*) = 1, true, false) as sessions_exists from sessions where sessionId = 'user1';
-- verificar si la sesion tiene mas de 30 minutos de antiguedad
select clientId, sessionId, createdat, if(minute(timediff(utc_timestamp(), createdat)) <= 30, "ACTIVE", "INACTIVE") as sessionStatus from sessions order by createdat asc;
-- total de minutos de una sesion
select clientId, sessionId, minute(timediff(utc_timestamp(), createdat)) from sessions where clientId = 'user3';

-- --------------------------
-- PROCEDIMIENTOS ALMACENADOS
-- --------------------------

drop procedure if exists create_session;
delimiter $$
create procedure create_session(in client_id_param varchar(50), in session_ttl_param int)
begin
	declare client_id_exists bool;
    declare session_exists bool;
    declare session_diff int;
    
    select if(count(*) = 1, true, false) into client_id_exists from clients where clientId = client_id_param;
    
    if client_id_exists = true then
		-- select 'existe';
        select if(count(*) = 1, true, false) into session_exists from sessions where clientId = client_id_param;
        if session_exists then
			-- select 'cliente existe, sesion existe';
            select minute(timediff(utc_timestamp(), createdat)) into session_diff from sessions where clientId = client_id_param;
            if session_diff>=session_ttl_param then
				start transaction;
                update sessions set createdat = utc_timestamp() where clientId = client_id_param;
                commit;
            end if;
		else
			-- select 'cliente existe, sesion NO existe';
            start transaction;
            insert into sessions(clientId, sessionId, createdat) values(client_id_param, uuid(), utc_timestamp());
            commit;
		end if;
	end if;
    select clientId,sessionId,createdat, if(minute(timediff(utc_timestamp(), createdat)) <= 30, "ACTIVE", "INACTIVE") as sessionStatus from sessions where clientId = client_id_param;
end
$$

-- cliente no existe
call create_session('user10',30);
-- sin sesion
call create_session('user2',30);
-- existe cliente, sesion y sesion inactiva
call create_session('user3',30);


drop procedure if exists validate_session;
delimiter $$
create procedure validate_session(in session_id_param varchar(50))
begin
	declare session_exists bool;
    
    select if(count(*) = 1, true, false) into session_exists from sessions where sessionId = session_id_param;
    if session_exists = true then
		-- select 'sesion existe';
        start transaction;
        select clientId,sessionId,createdat, if(minute(timediff(utc_timestamp(), createdat)) <= 30, "ACTIVE", "INACTIVE") as sessionStatus from sessions where sessionId = session_id_param;
        commit;
    end if;
end
$$

-- sesion no existe
call validate_session('sess1');
-- sesion existe y es valida
call validate_session('session2');





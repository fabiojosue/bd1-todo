-- -----------------------
-- FABIO CALDERON TORRES
-- CARNET 2018156314
-- MYSQL SCRIPT PROYECTO 2
-- -----------------------

DROP DATABASE IF EXISTS todosocial;
CREATE DATABASE todosocial;
USE todosocial;

CREATE USER IF NOT EXISTS 'todosocialuser' IDENTIFIED BY 'todosocialpass';
GRANT ALL PRIVILEGES ON todosocial.* TO 'todosocialuser';

-- ------------------
-- CREACION DE TABLAS
-- ------------------

CREATE TABLE Rating(
	id INT PRIMARY KEY AUTO_INCREMENT,
	todoId VARCHAR(50) NOT NULL,
    createdAt TIMESTAMP NOT NULL,
    calification ENUM('1','2','3','4','5') NOT NULL,
    userId VARCHAR(50) NOT NULL
);

CREATE TABLE Review(
	id INT PRIMARY KEY AUTO_INCREMENT,
	todoId VARCHAR(50) NOT NULL,
    createdAt TIMESTAMP NOT NULL,
    reviewText VARCHAR(200) NOT NULL,
    userId VARCHAR(50) NOT NULL
);

CREATE TABLE Image(
	id INT PRIMARY KEY AUTO_INCREMENT,
    reviewId INT NOT NULL,
    createdAt TIMESTAMP NOT NULL,
    userId VARCHAR(50) NOT NULL,
    url VARCHAR(200) NOT NULL,
    FOREIGN KEY(reviewId)
		REFERENCES Review(id)
);

-- --------------------------
-- PROCEDIMIENTOS ALMACENADOS
-- --------------------------

-- -------
-- RATINGS
-- -------

DROP PROCEDURE IF EXISTS create_rating;
delimiter $$
CREATE PROCEDURE create_rating(
	IN todoIdP VARCHAR(50),
    IN createdAtP TIMESTAMP,
	IN calificationP INT,
    IN userId VARCHAR(50)
)
BEGIN
	DECLARE errno INT;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
    GET CURRENT DIAGNOSTICS CONDITION 1 errno = MYSQL_ERRNO;
    SELECT errno AS MYSQL_ERROR;
    ROLLBACK;
    END;
    
    START TRANSACTION;
    
    INSERT INTO Rating(todoId, createdAt, calification, userId)
    VALUES(todoIdP, createdAtP, calificationP, userId);
    
    COMMIT;
    
END
$$

DROP PROCEDURE IF EXISTS delete_rating;
delimiter $$
CREATE PROCEDURE delete_rating(
	IN userP VARCHAR(50),
    IN todoIdP VARCHAR(50)
)
BEGIN
	DECLARE errno INT;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
    GET CURRENT DIAGNOSTICS CONDITION 1 errno = MYSQL_ERRNO;
    SELECT errno AS MYSQL_ERROR;
    ROLLBACK;
    END;
    
    START TRANSACTION;
    
    DELETE FROM Rating
    WHERE todoId = todoIdP
    AND userId = userP;
    
    COMMIT;
    
END
$$

DROP PROCEDURE IF EXISTS update_rating;
delimiter $$
CREATE PROCEDURE update_rating(
	IN idP INT,
    IN calificationP INT
)
BEGIN
	DECLARE errno INT;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
    GET CURRENT DIAGNOSTICS CONDITION 1 errno = MYSQL_ERRNO;
    SELECT errno AS MYSQL_ERROR;
    ROLLBACK;
    END;
    
    START TRANSACTION;
    
    UPDATE Rating
    SET
		calification = CalificationP 
	WHERE id = idP;
    
    COMMIT;
    
END
$$

DROP PROCEDURE IF EXISTS rating_avg;
delimiter $$
CREATE PROCEDURE rating_avg(
	IN todoIdP VARCHAR (50)
)
BEGIN
	DECLARE errno INT;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
    GET CURRENT DIAGNOSTICS CONDITION 1 errno = MYSQL_ERRNO;
    SELECT errno AS MYSQL_ERROR;
    ROLLBACK;
    END;
    
    START TRANSACTION;
    
    SELECT AVG(calification) as ratingAvg from Rating WHERE todoId = todoIdP;
    
    COMMIT;
END
$$

DROP PROCEDURE IF EXISTS find_rating;
delimiter $$
CREATE PROCEDURE find_rating(
	IN userP VARCHAR(50),
    IN todoIdP VARCHAR(50)
)
BEGIN
	DECLARE errno INT;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
    GET CURRENT DIAGNOSTICS CONDITION 1 errno = MYSQL_ERRNO;
    SELECT errno AS MYSQL_ERROR;
    ROLLBACK;
    END;
    
    START TRANSACTION;
    
    SELECT todoId FROM Rating WHERE userId = userP AND todoId = todoIdP;
    
    COMMIT;
END
$$

-- -------
-- REVIEWS
-- -------

DROP PROCEDURE IF EXISTS create_review;
delimiter $$
CREATE PROCEDURE create_review(
	IN todoIdP VARCHAR(50),
    IN createdAtP TIMESTAMP,
    IN textP VARCHAR(200),
    IN userId VARCHAR(50)
)
BEGIN
	DECLARE errno INT;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
    GET CURRENT DIAGNOSTICS CONDITION 1 errno = MYSQL_ERRNO;
    SELECT errno AS MYSQL_ERROR;
    ROLLBACK;
    END;
    
    START TRANSACTION;
    
    INSERT INTO Review(todoId, createdAt, reviewText, userId)
    VALUES(todoIdP, createdAtP, textP, userId);
    
    COMMIT;
    
END
$$

DROP PROCEDURE IF EXISTS delete_review;
delimiter $$
CREATE PROCEDURE delete_review(
	IN userP VARCHAR(50),
    IN todoIdP VARCHAR(50)
)
BEGIN
	DECLARE errno INT;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
    GET CURRENT DIAGNOSTICS CONDITION 1 errno = MYSQL_ERRNO;
    SELECT errno AS MYSQL_ERROR;
    ROLLBACK;
    END;
    
    START TRANSACTION;
    
    DELETE FROM Review
    WHERE userId = userP
    AND todoId = todoIdP;
    
    COMMIT;
    
END
$$
 
DROP PROCEDURE IF EXISTS update_review;
delimiter $$
CREATE PROCEDURE update_review(
	IN userIdP VARCHAR(50),
    IN textP VARCHAR(200),
    IN todoIdP VARCHAR(50),
    IN dateP TIMESTAMP
)
BEGIN
	DECLARE errno INT;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
    GET CURRENT DIAGNOSTICS CONDITION 1 errno = MYSQL_ERRNO;
    SELECT errno AS MYSQL_ERROR;
    ROLLBACK;
    END;
    
    START TRANSACTION;
    
    UPDATE Review
    SET
		reviewText = textP, 
        createdAt = dateP
	WHERE userId = userIdP
    AND todoId = todoIdP;
    
    COMMIT;
    
END
$$ 

DROP PROCEDURE IF EXISTS get_reviews;
delimiter $$
CREATE PROCEDURE get_reviews(
	IN todoIdP VARCHAR(50)
)
BEGIN
	DECLARE errno INT;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
    GET CURRENT DIAGNOSTICS CONDITION 1 errno = MYSQL_ERRNO;
    SELECT errno AS MYSQL_ERROR;
    ROLLBACK;
    END;
    
    START TRANSACTION;
    
	SELECT id, reviewText, userId, createdAt, todoId
    FROM Review
    WHERE todoId=todoIdP;
    
    COMMIT;
END
$$

DROP PROCEDURE IF EXISTS get_img_review;
delimiter $$
CREATE PROCEDURE get_img_review(
	IN userIdP VARCHAR(50),
	IN todoIdP VARCHAR(50)
)
BEGIN
	DECLARE errno INT;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
    GET CURRENT DIAGNOSTICS CONDITION 1 errno = MYSQL_ERRNO;
    SELECT errno AS MYSQL_ERROR;
    ROLLBACK;
    END;
    
    START TRANSACTION;
    
    SELECT i.url 
	FROM Review r
	LEFT OUTER JOIN Image i
	ON i.reviewId = r.id 
	WHERE r.userId=userIdP 
	AND r.todoId=todoIdP;
    
    COMMIT;
END
$$

DROP PROCEDURE IF EXISTS find_review;
delimiter $$
CREATE PROCEDURE find_review(
	IN userP VARCHAR(50),
    IN todoIdP VARCHAR(50)
)
BEGIN
	DECLARE errno INT;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
    GET CURRENT DIAGNOSTICS CONDITION 1 errno = MYSQL_ERRNO;
    SELECT errno AS MYSQL_ERROR;
    ROLLBACK;
    END;
    
    START TRANSACTION;
    
    SELECT todoId FROM Review WHERE userId = userP AND todoId = todoIdP;
    
    COMMIT;
END
$$

-- ------
-- IMAGES
-- ------

DROP PROCEDURE IF EXISTS create_image;
delimiter $$
CREATE PROCEDURE create_image(
	IN todoIdP VARCHAR(50),
    IN createdAtP TIMESTAMP,
    IN urlP VARCHAR(200),
    IN userIdP VARCHAR(50)
)
BEGIN
	DECLARE reviewIdP int;

	DECLARE errno INT;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
    GET CURRENT DIAGNOSTICS CONDITION 1 errno = MYSQL_ERRNO;
    SELECT errno AS MYSQL_ERROR;
    ROLLBACK;
    END;
    
    START TRANSACTION;
    SELECT id FROM review WHERE userId=userIdP AND todoId=todoIdP INTO reviewIdP;
    
    INSERT INTO Image(reviewId, createdAt, userId, url)
    VALUES(reviewIdP, createdAtP, userIdP, urlP);
    
    COMMIT;
    
END
$$

DROP PROCEDURE IF EXISTS count_images;
delimiter $$
CREATE PROCEDURE count_images(
	IN todoIdP VARCHAR(50),
    IN userIdP VARCHAR(50)
)
BEGIN
	DECLARE reviewIdP int;
	DECLARE errno INT;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
    GET CURRENT DIAGNOSTICS CONDITION 1 errno = MYSQL_ERRNO;
    SELECT errno AS MYSQL_ERROR;
    ROLLBACK;
    END;
    
    START TRANSACTION;
		SELECT id FROM review WHERE userId=userIdP AND todoId=todoIdP INTO reviewIdP;
		SELECT COUNT(reviewId) AS totalImg FROM Image WHERE reviewId = reviewIdP AND userId= userIdP;
    COMMIT;
    
END
$$

call create_rating('todo-1',utc_time(),5,'user3');
call create_rating('todo-1',utc_time(),5,'user2');
call create_rating('todo-1',utc_time(),3,'user1');
call create_rating('todo-3',utc_time(),3,'user3');
call create_rating('todo-3',utc_time(),2,'user2');
call create_rating('todo-3',utc_time(),1,'user1');
call create_rating('todo-5',utc_time(),5,'user3');
call create_rating('todo-5',utc_time(),4,'user2');
call create_rating('todo-5',utc_time(),1,'user1');

call create_review('todo-1',utc_time(),'Excelente','user3');
call create_review('todo-1',utc_time(),'Lo mejor que he visto','user2');
call create_review('todo-1',utc_time(),'Pudo ser mejor','user1');
call create_review('todo-3',utc_time(),'No es lo que esperaba','user3');
call create_review('todo-3',utc_time(),'Pésimo','user2');
call create_review('todo-3',utc_time(),'No existe algo peor','user1');
call create_review('todo-5',utc_time(),'Me encanta','user3');
call create_review('todo-5',utc_time(),'Casi perfecto','user2');
call create_review('todo-5',utc_time(),'Qué es esto?','user1');

call create_image('todo-1',utc_time(),'http://localhost:8082/reviews/100/img.png','user3');
call create_image('todo-1',utc_time(),'http://localhost:8082/reviews/101/img2.jpg','user3');
call create_image('todo-1',utc_time(),'http://localhost:8082/reviews/102/img3.png','user2');
call create_image('todo-1',utc_time(),'http://localhost:8082/reviews/103/img4.jpg','user1');
call create_image('todo-3',utc_time(),'http://localhost:8082/reviews/104/img5.png','user3');
call create_image('todo-3',utc_time(),'http://localhost:8082/reviews/105/img6.jpg','user3');
call create_image('todo-3',utc_time(),'http://localhost:8082/reviews/106/img7.png','user2');

-- select * from Rating;
-- select * from Review;
-- select * from Image;


-- left join ejemplo
-- SELECT r.reviewText, r.userId, r.createdAt, i.url 
-- FROM Review r
-- LEFT OUTER JOIN Image i
-- ON i.reviewId = r.id 
-- WHERE r.userId='user3' 
-- AND r.todoId='todo-1';










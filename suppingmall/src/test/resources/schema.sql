-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema suppingmalltest
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema suppingmalltest
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `suppingmalltest` DEFAULT CHARACTER SET utf8 ;
USE `suppingmalltest` ;

-- -----------------------------------------------------
-- Table `suppingmalltest`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `suppingmalltest`.`user` (
  `user_id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '유저번호',
  `email` VARCHAR(45) NOT NULL COMMENT '이메일',
  `password` VARCHAR(255) NULL DEFAULT NULL COMMENT '비밀번호',
  `name` VARCHAR(45) NOT NULL COMMENT '이름',
  `nickname` VARCHAR(45) NOT NULL COMMENT '별명',
  `address` VARCHAR(100) NULL DEFAULT NULL COMMENT '주소1',
  `address_detail` VARCHAR(100) NULL DEFAULT NULL COMMENT '주소2',
  `zipcode` VARCHAR(45) NULL DEFAULT NULL COMMENT '우편번호',
  `phone_number` VARCHAR(45) NULL DEFAULT NULL COMMENT '핸드폰번호',
  `created_date` TIMESTAMP(3) NULL DEFAULT NULL COMMENT '가입일',
  `updated_date` TIMESTAMP(3) NULL DEFAULT NULL COMMENT '수정일',
  `delete_yes_or_no` CHAR(1) NOT NULL COMMENT '삭제여부',
  `role` VARCHAR(4) NOT NULL COMMENT '권한(코드)',
  `store_name` VARCHAR(45) NULL DEFAULT NULL COMMENT '판매자명',
  `store_phone_number` VARCHAR(45) NULL DEFAULT NULL COMMENT '판매자전화번호',
  `store_address` VARCHAR(100) NULL DEFAULT NULL COMMENT '판매자주소1',
  `store_address_detail` VARCHAR(100) NULL DEFAULT NULL COMMENT '판매자주소2',
  `store_zipcode` VARCHAR(45) NULL DEFAULT NULL COMMENT '판매자우편번호',
  `type` VARCHAR(4) NOT NULL,
  `store_private_number` VARCHAR(45) NULL DEFAULT NULL,
  `store_apply_yes_or_no` CHAR(1) NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE INDEX `email` (`email` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 24
DEFAULT CHARACTER SET = utf8
COMMENT = '회원';


-- -----------------------------------------------------
-- Table `suppingmalltest`.`cart`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `suppingmalltest`.`cart` (
  `cart_id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '장바구니번호',
  `product_id` BIGINT(20) NOT NULL COMMENT '물품번호',
  `product_option_id` INT(11) NOT NULL COMMENT '물품옵션번호(코드)',
  `buyer_id` BIGINT(20) NOT NULL,
  `quantity` INT(11) NOT NULL COMMENT '판매수량',
  `create_date` VARCHAR(45) NOT NULL,
  `seller_id` BIGINT(20) NOT NULL,
  PRIMARY KEY (`cart_id`),
  INDEX `fk_cart_buyer_id_idx` (`buyer_id` ASC) VISIBLE,
  INDEX `fk_cart_seller_id_idx` (`seller_id` ASC) VISIBLE,
  CONSTRAINT `fk_cart_buyer_id`
    FOREIGN KEY (`buyer_id`)
    REFERENCES `suppingmalltest`.`user` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_cart_seller_id`
    FOREIGN KEY (`seller_id`)
    REFERENCES `suppingmalltest`.`user` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = '장바구니';


-- -----------------------------------------------------
-- Table `suppingmalltest`.`category`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `suppingmalltest`.`category` (
  `category_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `parent_id` BIGINT(20) NULL DEFAULT NULL,
  `name` VARCHAR(45) NOT NULL,
  `child` BIGINT(20) NULL DEFAULT NULL,
  `memo` VARCHAR(500) NULL DEFAULT NULL,
  PRIMARY KEY (`category_id`),
  INDEX `fk_parent_id_idx` (`parent_id` ASC) VISIBLE,
  CONSTRAINT `fk_parent_id`
    FOREIGN KEY (`parent_id`)
    REFERENCES `suppingmalltest`.`category` (`category_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 31
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `suppingmalltest`.`product`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `suppingmalltest`.`product` (
  `product_id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '물품번호',
  `name` VARCHAR(45) NOT NULL COMMENT '물품명',
  `price` INT(10) NOT NULL COMMENT '가격',
  `registered_date` TIMESTAMP(3) NOT NULL COMMENT '등록일',
  `seller_id` BIGINT(20) NOT NULL COMMENT '판매자',
  `category_id` BIGINT(20) NOT NULL COMMENT '카테고리(코드)',
  `thumbnail` VARCHAR(100) NULL DEFAULT NULL COMMENT '썸네일',
  `picture` VARCHAR(100) NULL DEFAULT NULL COMMENT '물품사진',
  `sale_yes_or_no` VARCHAR(45) NOT NULL COMMENT '판매 여부',
  `contents` VARCHAR(2000) NULL DEFAULT NULL,
  `rating` INT(5) NULL DEFAULT NULL,
  `delivery_price` INT(11) NOT NULL,
  `delivery_vendor` CHAR(4) NOT NULL,
  PRIMARY KEY (`product_id`),
  INDEX `fk_user_id_idx` (`seller_id` ASC) VISIBLE,
  INDEX `fk_product_category_id` (`category_id` ASC) VISIBLE,
  CONSTRAINT `fk_product_category_id`
    FOREIGN KEY (`category_id`)
    REFERENCES `suppingmalltest`.`category` (`category_id`),
  CONSTRAINT `fk_product_user_id`
    FOREIGN KEY (`seller_id`)
    REFERENCES `suppingmalltest`.`user` (`user_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 25
DEFAULT CHARACTER SET = utf8
COMMENT = '판매물품';


-- -----------------------------------------------------
-- Table `suppingmalltest`.`board`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `suppingmalltest`.`board` (
  `board_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(100) NOT NULL,
  `contents` VARCHAR(1000) NULL DEFAULT NULL,
  `category_id` BIGINT(20) NOT NULL,
  `user_id` BIGINT(20) NOT NULL,
  `product_id` BIGINT(20) NULL DEFAULT NULL,
  `created_date` TIMESTAMP(3) NOT NULL,
  `updated_date` TIMESTAMP(3) NOT NULL,
  `hit` INT(11) NULL DEFAULT NULL,
  PRIMARY KEY (`board_id`),
  INDEX `fk_user_id_idx` (`user_id` ASC) VISIBLE,
  INDEX `fk_product_id_idx` (`product_id` ASC) VISIBLE,
  INDEX `fk_board_category_id` (`category_id` ASC) VISIBLE,
  CONSTRAINT `fk_board_category_id`
    FOREIGN KEY (`category_id`)
    REFERENCES `suppingmalltest`.`category` (`category_id`),
  CONSTRAINT `fk_board_product_id`
    FOREIGN KEY (`product_id`)
    REFERENCES `suppingmalltest`.`product` (`product_id`),
  CONSTRAINT `fk_user_id`
    FOREIGN KEY (`user_id`)
    REFERENCES `suppingmalltest`.`user` (`user_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 74
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `suppingmalltest`.`comment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `suppingmalltest`.`comment` (
  `comment_id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '댓글번호',
  `level` INT(11) NOT NULL COMMENT '수준',
  `title` VARCHAR(45) NULL DEFAULT NULL COMMENT '제목',
  `contents` VARCHAR(500) NOT NULL COMMENT '내용',
  `score` INT(11) NULL DEFAULT NULL COMMENT '평점',
  `user_id` BIGINT(20) NOT NULL COMMENT '작성자',
  `board_id` BIGINT(20) NOT NULL COMMENT '게시글',
  `created_date` TIMESTAMP(3) NOT NULL COMMENT '작성일',
  `updated_date` TIMESTAMP(3) NOT NULL COMMENT '수정일',
  `secret_yes_or_no` CHAR(1) NULL DEFAULT NULL COMMENT '비밀여부',
  `parent_id` BIGINT(20) NULL DEFAULT NULL,
  PRIMARY KEY (`comment_id`),
  INDEX `fk_product_id_user_id_idx` (`user_id` ASC) VISIBLE,
  INDEX `fk_board_id_board_id_idx` (`board_id` ASC) VISIBLE,
  CONSTRAINT `fk_comment_board_id`
    FOREIGN KEY (`board_id`)
    REFERENCES `suppingmalltest`.`board` (`board_id`),
  CONSTRAINT `fk_comment_user_id`
    FOREIGN KEY (`user_id`)
    REFERENCES `suppingmalltest`.`user` (`user_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 7
DEFAULT CHARACTER SET = utf8
COMMENT = '댓글';


-- -----------------------------------------------------
-- Table `suppingmalltest`.`delivery`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `suppingmalltest`.`delivery` (
  `delivery_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `address` VARCHAR(45) NOT NULL,
  `address_detail` VARCHAR(45) NULL DEFAULT NULL,
  `zipcode` VARCHAR(45) NOT NULL,
  `memo` VARCHAR(500) NULL DEFAULT NULL,
  `phone` VARCHAR(45) NOT NULL,
  `vendor` VARCHAR(45) NULL DEFAULT NULL,
  `tracking_number` VARCHAR(45) NULL DEFAULT NULL,
  `name` VARCHAR(45) NOT NULL,
  `status` CHAR(4) NULL DEFAULT NULL,
  `create_date` TIMESTAMP(3) NOT NULL,
  `updated_date` TIMESTAMP(3) NULL DEFAULT NULL,
  PRIMARY KEY (`delivery_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 44
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `suppingmalltest`.`payment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `suppingmalltest`.`payment` (
  `payment_id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '결제번호',
  `price` VARCHAR(45) NOT NULL COMMENT '결제금액',
  `type` CHAR(4) NOT NULL COMMENT '결제유형(카드,무통장,페이)',
  `vendor_check_number` VARCHAR(45) NULL DEFAULT NULL,
  `status` VARCHAR(45) NULL DEFAULT NULL,
  `pay_date` VARCHAR(45) NULL DEFAULT NULL,
  `updated_date` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`payment_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 58
DEFAULT CHARACTER SET = utf8
COMMENT = '결제이력';


-- -----------------------------------------------------
-- Table `suppingmalltest`.`orders`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `suppingmalltest`.`orders` (
  `order_id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '주문번호',
  `order_date` TIMESTAMP(3) NOT NULL COMMENT '구매일',
  `buyer_id` BIGINT(20) NOT NULL COMMENT '구매자',
  `delivery_id` BIGINT(20) NULL DEFAULT NULL COMMENT '운송정보아이디',
  `status` CHAR(4) NULL DEFAULT NULL COMMENT '상태',
  `payment_id` BIGINT(20) NULL DEFAULT NULL,
  `update_date` TIMESTAMP(3) NULL DEFAULT NULL,
  `seller_id` BIGINT(20) NOT NULL,
  PRIMARY KEY (`order_id`),
  INDEX `fk_user_id_idx` (`buyer_id` ASC) VISIBLE,
  INDEX `fk_delivery_id_idx` (`delivery_id` ASC) VISIBLE,
  INDEX `fk_order_payment_id_idx` (`payment_id` ASC) VISIBLE,
  INDEX `fk_order_seller_id_idx` (`seller_id` ASC) VISIBLE,
  CONSTRAINT `fk_order_buyer_id`
    FOREIGN KEY (`buyer_id`)
    REFERENCES `suppingmalltest`.`user` (`user_id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `fk_order_delivery_id`
    FOREIGN KEY (`delivery_id`)
    REFERENCES `suppingmalltest`.`delivery` (`delivery_id`)
    ON DELETE SET NULL
    ON UPDATE SET NULL,
  CONSTRAINT `fk_order_payment_id`
    FOREIGN KEY (`payment_id`)
    REFERENCES `suppingmalltest`.`payment` (`payment_id`)
    ON DELETE SET NULL
    ON UPDATE SET NULL,
  CONSTRAINT `fk_order_seller_id`
    FOREIGN KEY (`seller_id`)
    REFERENCES `suppingmalltest`.`user` (`user_id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT)
ENGINE = InnoDB
AUTO_INCREMENT = 159
DEFAULT CHARACTER SET = utf8
COMMENT = '주문';


-- -----------------------------------------------------
-- Table `suppingmalltest`.`product_option`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `suppingmalltest`.`product_option` (
  `product_id` BIGINT(20) NOT NULL COMMENT '물품번호',
  `product_option_id` INT(11) NOT NULL COMMENT '물품옵션번호',
  `name` VARCHAR(45) NOT NULL COMMENT '물품상세이름',
  `price` VARCHAR(45) NOT NULL COMMENT '가격(원가격에 더해짐)',
  `quantity` INT(11) NOT NULL COMMENT '총판매수량',
  `spec_1` VARCHAR(45) NULL DEFAULT NULL,
  `spec_2` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`product_id`, `product_option_id`),
  CONSTRAINT `fk_product_id`
    FOREIGN KEY (`product_id`)
    REFERENCES `suppingmalltest`.`product` (`product_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = '판매물품옵션';


-- -----------------------------------------------------
-- Table `suppingmalltest`.`orderItem`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `suppingmalltest`.`orderItem` (
  `orderitem_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `order_id` BIGINT(20) NOT NULL,
  `product_id` BIGINT(20) NOT NULL,
  `product_option_id` INT(11) NOT NULL,
  `count` INT(11) NOT NULL,
  `price` INT(11) NOT NULL,
  PRIMARY KEY (`orderitem_id`),
  INDEX `fk_order_id_idx` (`order_id` ASC) VISIBLE,
  INDEX `fk_product_id_product_option_id_idx` (`product_id` ASC, `product_option_id` ASC) VISIBLE,
  CONSTRAINT `fk_order_item_order_id`
    FOREIGN KEY (`order_id`)
    REFERENCES `suppingmalltest`.`orders` (`order_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_order_item_product_id_product_option_id`
    FOREIGN KEY (`product_id` , `product_option_id`)
    REFERENCES `suppingmalltest`.`product_option` (`product_id` , `product_option_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 270
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `suppingmalltest`.`product_detail`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `suppingmalltest`.`product_detail` (
  `product_id` BIGINT(20) NOT NULL COMMENT '물품번호',
  `product_detail_id` INT(11) NOT NULL COMMENT '물품상세번호',
  `spec_1` VARCHAR(45) NULL DEFAULT NULL,
  `spec_2` VARCHAR(45) NULL DEFAULT NULL,
  `spec_3` VARCHAR(45) NULL DEFAULT NULL,
  `spec_4` VARCHAR(45) NULL DEFAULT NULL,
  `spec_5` VARCHAR(45) NULL DEFAULT NULL,
  `spec_6` VARCHAR(45) NULL DEFAULT NULL,
  `spec_7` VARCHAR(45) NULL DEFAULT NULL,
  `spec_8` VARCHAR(45) NULL DEFAULT NULL,
  `spec_9` VARCHAR(45) NULL DEFAULT NULL,
  `spec_10` VARCHAR(45) NULL DEFAULT NULL,
  `manuplated_date` DATETIME NULL DEFAULT NULL,
  `as_number` VARCHAR(45) NULL DEFAULT NULL,
  `contry_of_origin` VARCHAR(45) NULL DEFAULT NULL,
  `manufacturer` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`product_id`, `product_detail_id`),
  CONSTRAINT `fk_detail_product_id`
    FOREIGN KEY (`product_id`)
    REFERENCES `suppingmalltest`.`product` (`product_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = '판매물품옵션';


-- -----------------------------------------------------
-- Table `suppingmalltest`.`cartItem`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `suppingmalltest`.`cartItem` (
  `cartItem_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `cart_id` BIGINT(20) NOT NULL,
  `product_id` BIGINT(20) NOT NULL,
  `product_option_id` INT(11) NOT NULL,
  `quantity` INT(11) NOT NULL,
  `price` INT(11) NOT NULL,
  PRIMARY KEY (`cartItem_id`),
  INDEX `fk_cartItem_product_id_product_option_id_idx` (`product_id` ASC, `product_option_id` ASC) VISIBLE,
  INDEX `fk_cartItem_cart_id_idx` (`cart_id` ASC) VISIBLE,
  CONSTRAINT `fk_cartItem_product_id_product_option_id`
    FOREIGN KEY (`product_id` , `product_option_id`)
    REFERENCES `suppingmalltest`.`product_option` (`product_id` , `product_option_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_cartItem_cart_id`
    FOREIGN KEY (`cart_id`)
    REFERENCES `suppingmalltest`.`cart` (`cart_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

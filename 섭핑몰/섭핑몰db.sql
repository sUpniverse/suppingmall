-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema suppingmall
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema suppingmall
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `suppingmall` DEFAULT CHARACTER SET utf8 ;
USE `suppingmall` ;

-- -----------------------------------------------------
-- Table `suppingmall`.`category`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `suppingmall`.`category` (
  `category_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `parent_id` BIGINT(20) NULL DEFAULT NULL,
  `name` VARCHAR(45) NOT NULL,
  `child` BIGINT(20) NULL DEFAULT NULL,
  `memo` VARCHAR(500) NULL DEFAULT NULL,
  `en_name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`category_id`),
  INDEX `fk_parent_id_idx` (`parent_id` ASC) VISIBLE,
  CONSTRAINT `fk_parent_id`
    FOREIGN KEY (`parent_id`)
    REFERENCES `suppingmall`.`category` (`category_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 72
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `suppingmall`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `suppingmall`.`user` (
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
  `email_confirm_yes_or_no` CHAR(1) NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE INDEX `email` (`email` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 135
DEFAULT CHARACTER SET = utf8
COMMENT = '회원';


-- -----------------------------------------------------
-- Table `suppingmall`.`product`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `suppingmall`.`product` (
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
    REFERENCES `suppingmall`.`category` (`category_id`),
  CONSTRAINT `fk_product_user_id`
    FOREIGN KEY (`seller_id`)
    REFERENCES `suppingmall`.`user` (`user_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 66
DEFAULT CHARACTER SET = utf8
COMMENT = '판매물품';


-- -----------------------------------------------------
-- Table `suppingmall`.`board`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `suppingmall`.`board` (
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
    REFERENCES `suppingmall`.`category` (`category_id`),
  CONSTRAINT `fk_board_product_id`
    FOREIGN KEY (`product_id`)
    REFERENCES `suppingmall`.`product` (`product_id`),
  CONSTRAINT `fk_user_id`
    FOREIGN KEY (`user_id`)
    REFERENCES `suppingmall`.`user` (`user_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 74
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `suppingmall`.`cart`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `suppingmall`.`cart` (
  `cart_id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '장바구니번호',
  `buyer_id` BIGINT(20) NOT NULL,
  `create_date` TIMESTAMP NOT NULL,
  PRIMARY KEY (`cart_id`),
  INDEX `fk_cart_buyer_id_idx` (`buyer_id` ASC) VISIBLE,
  CONSTRAINT `fk_cart_buyer_id`
    FOREIGN KEY (`buyer_id`)
    REFERENCES `suppingmall`.`user` (`user_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 29
DEFAULT CHARACTER SET = utf8
COMMENT = '장바구니';


-- -----------------------------------------------------
-- Table `suppingmall`.`product_option`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `suppingmall`.`product_option` (
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
    REFERENCES `suppingmall`.`product` (`product_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = '판매물품옵션';


-- -----------------------------------------------------
-- Table `suppingmall`.`cartItem`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `suppingmall`.`cartItem` (
  `cartItem_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `cart_id` BIGINT(20) NOT NULL,
  `product_id` BIGINT(20) NOT NULL,
  `product_option_id` INT(11) NOT NULL,
  `quantity` INT(11) NOT NULL,
  `price` INT(11) NOT NULL,
  PRIMARY KEY (`cartItem_id`),
  INDEX `fk_cartItem_product_id_product_option_id_idx` (`product_id` ASC, `product_option_id` ASC) VISIBLE,
  INDEX `fk_cartItem_cart_id_idx` (`cart_id` ASC) VISIBLE,
  CONSTRAINT `fk_cartItem_cart_id`
    FOREIGN KEY (`cart_id`)
    REFERENCES `suppingmall`.`cart` (`cart_id`),
  CONSTRAINT `fk_cartItem_product_id_product_option_id`
    FOREIGN KEY (`product_id` , `product_option_id`)
    REFERENCES `suppingmall`.`product_option` (`product_id` , `product_option_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 30
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `suppingmall`.`comment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `suppingmall`.`comment` (
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
    REFERENCES `suppingmall`.`board` (`board_id`),
  CONSTRAINT `fk_comment_user_id`
    FOREIGN KEY (`user_id`)
    REFERENCES `suppingmall`.`user` (`user_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 16
DEFAULT CHARACTER SET = utf8
COMMENT = '댓글';


-- -----------------------------------------------------
-- Table `suppingmall`.`delivery`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `suppingmall`.`delivery` (
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
AUTO_INCREMENT = 46
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `suppingmall`.`payment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `suppingmall`.`payment` (
  `payment_id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '결제번호',
  `price` VARCHAR(45) NOT NULL COMMENT '결제금액',
  `type` CHAR(4) NOT NULL COMMENT '결제유형(카드,무통장,페이)',
  `vendor_check_number` VARCHAR(45) NULL DEFAULT NULL,
  `status` VARCHAR(45) NULL DEFAULT NULL,
  `pay_date` VARCHAR(45) NULL DEFAULT NULL,
  `updated_date` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`payment_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 60
DEFAULT CHARACTER SET = utf8
COMMENT = '결제이력';


-- -----------------------------------------------------
-- Table `suppingmall`.`orders`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `suppingmall`.`orders` (
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
    REFERENCES `suppingmall`.`user` (`user_id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `fk_order_delivery_id`
    FOREIGN KEY (`delivery_id`)
    REFERENCES `suppingmall`.`delivery` (`delivery_id`)
    ON DELETE SET NULL
    ON UPDATE SET NULL,
  CONSTRAINT `fk_order_payment_id`
    FOREIGN KEY (`payment_id`)
    REFERENCES `suppingmall`.`payment` (`payment_id`)
    ON DELETE SET NULL
    ON UPDATE SET NULL,
  CONSTRAINT `fk_order_seller_id`
    FOREIGN KEY (`seller_id`)
    REFERENCES `suppingmall`.`user` (`user_id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT)
ENGINE = InnoDB
AUTO_INCREMENT = 162
DEFAULT CHARACTER SET = utf8
COMMENT = '주문';


-- -----------------------------------------------------
-- Table `suppingmall`.`orderItem`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `suppingmall`.`orderItem` (
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
    REFERENCES `suppingmall`.`orders` (`order_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_order_item_product_id_product_option_id`
    FOREIGN KEY (`product_id` , `product_option_id`)
    REFERENCES `suppingmall`.`product_option` (`product_id` , `product_option_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 273
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `suppingmall`.`product_detail`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `suppingmall`.`product_detail` (
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
    REFERENCES `suppingmall`.`product` (`product_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = '판매물품옵션';


-- -----------------------------------------------------
-- Table `suppingmall`.`user_confirmation`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `suppingmall`.`user_confirmation` (
  `user_confirm_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT(20) NOT NULL,
  `send_date` TIMESTAMP(3) NULL DEFAULT NULL,
  `confirm_date` TIMESTAMP(3) NULL DEFAULT NULL,
  `confirm_token` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`user_confirm_id`, `user_id`),
  INDEX `fk_confirm_user_id_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `fk_confirm_user_id`
    FOREIGN KEY (`user_id`)
    REFERENCES `suppingmall`.`user` (`user_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 3
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS users;

CREATE TABLE users (
  id                    INT(11) NOT NULL AUTO_INCREMENT,
  username              VARCHAR(50) NOT NULL,
  password              CHAR(80) NOT NULL,
  first_name            VARCHAR(50) NOT NULL,
  last_name             VARCHAR(50) NOT NULL,
  email                 VARCHAR(50) NOT NULL,
  phone                 VARCHAR(15) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS roles;

CREATE TABLE roles (
  id                    INT(11) NOT NULL AUTO_INCREMENT,
  name                  VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS users_roles;

CREATE TABLE users_roles (
  user_id               INT(11) NOT NULL,
  role_id               INT(11) NOT NULL,

  PRIMARY KEY (user_id, role_id),

--  KEY FK_ROLE_idx (role_id),

  CONSTRAINT FK_USER_ID_01 FOREIGN KEY (user_id)
  REFERENCES users (id)
  ON DELETE NO ACTION ON UPDATE NO ACTION,

  CONSTRAINT FK_ROLE_ID FOREIGN KEY (role_id)
  REFERENCES roles (id)
  ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS categories;

CREATE TABLE categories (
  id	                INT(11) NOT NULL AUTO_INCREMENT,
  title                 VARCHAR(255) NOT NULL,
  description           VARCHAR(5000),
  PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS products;

CREATE TABLE products (
  id	                INT(11) NOT NULL AUTO_INCREMENT,
  category_id           INT(11) NOT NULL,
  vendor_code           VARCHAR(8) NOT NULL,
  title                 VARCHAR(255) NOT NULL,
  short_description     VARCHAR(1000) NOT NULL,
  full_description      VARCHAR(5000) NOT NULL,
  price                 DECIMAL(8,2) NOT NULL,
  create_at             TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_at             TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  CONSTRAINT FK_CATEGORY_ID FOREIGN KEY (category_id)
  REFERENCES categories (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS products_images;

CREATE TABLE products_images (
  id                    INT(11) NOT NULL AUTO_INCREMENT,
  product_id            INT(11) NOT NULL,
  path                  VARCHAR(250) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT FK_PRODUCT_ID_IMG FOREIGN KEY (product_id)
  REFERENCES products (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS orders_statuses;

CREATE TABLE orders_statuses (
  id                    INT(11) NOT NULL AUTO_INCREMENT,
  title                 VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS delivery_addresses;

CREATE TABLE delivery_addresses (
  id	                INT(11) NOT NULL AUTO_INCREMENT,
  user_id               INT(11) NOT NULL,
  address               VARCHAR(500) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT FK_USER_ID_DEL_ADR FOREIGN KEY (user_id)
  REFERENCES users (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS orders;

CREATE TABLE orders (
  id	                INT(11) NOT NULL AUTO_INCREMENT,
  user_id               INT(11) NOT NULL,
  price                 DECIMAL(8,2) NOT NULL,
  delivery_price        DECIMAL(8,2) NOT NULL,
  delivery_address_id   INT(11) NOT NULL,
  phone_number          VARCHAR(20) NOT NULL,
  status                VARCHAR(12) NOT NULL,
  delivery_date         TIMESTAMP NOT NULL,
  create_at             TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_at             TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  CONSTRAINT FK_USER_ID FOREIGN KEY (user_id)
  REFERENCES users (id),
  CONSTRAINT FK_DELIVERY_ADDRESS_ID FOREIGN KEY (delivery_address_id)
  REFERENCES delivery_addresses (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS orders_item;

CREATE TABLE orders_item (
  id	                INT(11) NOT NULL AUTO_INCREMENT,
  product_id            INT(11) NOT NULL,
  order_id              INT(11) NOT NULL,
  quantity              INT NOT NULL,
  item_price            DECIMAL(8,2) NOT NULL,
  total_price           DECIMAL(8,2) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT FK_ORDER_ID FOREIGN KEY (order_id)
  REFERENCES orders (id),
  CONSTRAINT FK_PRODUCT_ID_ORD_IT FOREIGN KEY (product_id)
  REFERENCES products (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

SET FOREIGN_KEY_CHECKS = 1;


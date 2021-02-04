SET FOREIGN_KEY_CHECKS = 0;
INSERT INTO roles (name)
VALUES
('ROLE_EMPLOYEE'), ('ROLE_MANAGER'), ('ROLE_ADMIN');

INSERT INTO users (username,password,first_name,last_name,email,phone)
VALUES
('admin','$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i','Admin','Admin','admin@gmail.com','+79881111111');

INSERT INTO users_roles (user_id, role_id)
VALUES
(1, 1),
(1, 2),
(1, 3);
SET FOREIGN_KEY_CHECKS = 1;
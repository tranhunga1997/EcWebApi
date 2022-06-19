INSERT INTO permissions (id,create_datetime,permission_key,permission_name) VALUES (NEXTVAL('permission_seq'),NOW(),'view-role-info','xem thông tin role');
INSERT INTO permissions (id,create_datetime,permission_key,permission_name) VALUES (NEXTVAL('permission_seq'),NOW(),'create-role-info','thêm thông tin role mới');
INSERT INTO permissions (id,create_datetime,permission_key,permission_name) VALUES (NEXTVAL('permission_seq'),NOW(),'update-role-info','sửa thông tin role');
INSERT INTO permissions (id,create_datetime,permission_key,permission_name) VALUES (NEXTVAL('permission_seq'),NOW(),'delete-role-info','xóa thông tin role');
INSERT INTO permissions (id,create_datetime,permission_key,permission_name) VALUES (NEXTVAL('permission_seq'),NOW(),'user-block-status-change','khóa và mở khóa tài khoản');
INSERT INTO permissions (id,create_datetime,permission_key,permission_name) VALUES (NEXTVAL('permission_seq'),NOW(),'view-user-info','xem thông tin tài khoản');
INSERT INTO permissions (id,create_datetime,permission_key,permission_name) VALUES (NEXTVAL('permission_seq'),NOW(),'view-permission-info','xem thông tin permission');
INSERT INTO permissions (id,create_datetime,permission_key,permission_name) VALUES (NEXTVAL('permission_seq'),NOW(),'add-new-product-info','thêm thông tin sản phẩm mới');
INSERT INTO permissions (id,create_datetime,permission_key,permission_name) VALUES (NEXTVAL('permission_seq'),NOW(),'delete-product-info','xóa thông tin sản phẩm');
INSERT INTO permissions (id,create_datetime,permission_key,permission_name) VALUES (NEXTVAL('permission_seq'),NOW(),'update-product-info','sửa thông tin sản phẩm');
-- Insert roles
INSERT INTO tbl_role (id, name, description, created_at, updated_at) VALUES
(1, 'ROLE_ADMIN', 'Administrator role', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'ROLE_USER', 'User role', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert permissions
INSERT INTO tbl_permission (id, permissionName, description) VALUES
(1, 'READ_PRIVILEGES', 'Permission to read data'),
(2, 'WRITE_PRIVILEGES', 'Permission to write data');

-- Insert role-permission mappings
INSERT INTO role_permissions (role_id, permission_id) VALUES
(1, 1), -- ROLE_ADMIN -> READ_PRIVILEGES
(1, 2), -- ROLE_ADMIN -> WRITE_PRIVILEGES
(2, 1); -- ROLE_USER -> READ_PRIVILEGES

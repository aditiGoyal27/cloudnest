CREATE TABLE IF NOT EXISTS tbl_role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,    -- Auto-generated primary key
    name VARCHAR(255) NOT NULL,              -- Role name (required)
    description TEXT,                        -- Role description
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,   -- Created date (auto-set)
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP   -- Updated date (auto-set)
);

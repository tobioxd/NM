 SET DEFINE OFF;

DROP TABLE be_tokens;

DROP TABLE be_users;

CREATE TABLE be_users (
    id NVARCHAR2 (255) NOT NULL PRIMARY KEY,
    phone_number NVARCHAR2 (255),
    email NVARCHAR2 (255),
    password NVARCHAR2 (255),
    name NVARCHAR2 (255),
    role NVARCHAR2 (255),
    created_at DATE,
    is_active NUMBER (1) DEFAULT 1,
    photo_url NVARCHAR2 (255) DEFAULT 'default.jpg',
    password_reset_token NVARCHAR2(255),
    password_reset_expiration_date DATE,
    password_change_at DATE,
    CONSTRAINT chk_be_users_role CHECK (
        role IN (
            'admin',
            'user'
        )
    )
);

INSERT INTO be_users(id,phone_number, email, password, name, role) VALUES ('F0C8CB35F1434C8BABE8E736E36A1A0E','000000001', 'minhnhat.kd.hungyen1@gmail.com', '$2a$10$aI1HrbKMlpUI/aheMG.o7.Ygu7DVPHtEDdgXc.G.suyz2TVs5ehxq', 'admin', 'admin');    
INSERT INTO be_users(id,phone_number, email, password, name, role) VALUES ('F0C8CB35F1434C8BABE8E736E36A2A0E','000000002', 'minhnhat.kd.hungyen@gmail.com', '$2a$10$aI1HrbKMlpUI/aheMG.o7.Ygu7DVPHtEDdgXc.G.suyz2TVs5ehxq', 'user 1', 'user');
INSERT INTO be_users(id,phone_number, email, password, name, role) VALUES ('F0C8CB35F1434C8BABE8E736E36A3A0E','000000003', 'minhnhat.kd.hungyen2@gmail.com', '$2a$10$aI1HrbKMlpUI/aheMG.o7.Ygu7DVPHtEDdgXc.G.suyz2TVs5ehxq', 'user 2', 'user');
INSERT INTO be_users(id,phone_number, email, password, name, role) VALUES ('F0C8CB35F1434C8BABE8E736E36A4A0E','000000004', 'minhnhat.kd.hungyen3@gmail.com', '$2a$10$aI1HrbKMlpUI/aheMG.o7.Ygu7DVPHtEDdgXc.G.suyz2TVs5ehxq', 'user 3', 'user');
INSERT INTO be_users(id,phone_number, email, password, name, role) VALUES ('F0C8CB35F1434C8BABE8E736E31A4A0E','000000006', 'minhnhat.kd.hungyen4@gmail.com', '$2a$10$aI1HrbKMlpUI/aheMG.o7.Ygu7DVPHtEDdgXc.G.suyz2TVs5ehxq', 'user 4', 'user');
INSERT INTO be_users(id,phone_number, email, password, name, role) VALUES ('F0C8CB35F1434C8BABE8E736E32A4A0E','000000007', 'minhnhat.kd.hungyen5@gmail.com', '$2a$10$aI1HrbKMlpUI/aheMG.o7.Ygu7DVPHtEDdgXc.G.suyz2TVs5ehxq', 'user 5', 'user');
INSERT INTO be_users(id,phone_number, email, password, name, role) VALUES ('F0C8CB35F1434C8BABE8E736E33A4A0E','000000008', 'minhnhat.kd.hungyen6@gmail.com', '$2a$10$aI1HrbKMlpUI/aheMG.o7.Ygu7DVPHtEDdgXc.G.suyz2TVs5ehxq', 'user 6', 'user');
INSERT INTO be_users(id,phone_number, email, password, name, role) VALUES ('F0C8CB35F1434C8BABE8E736E34A4A0E','000000009', 'minhnhat.kd.hungyen7@gmail.com', '$2a$10$aI1HrbKMlpUI/aheMG.o7.Ygu7DVPHtEDdgXc.G.suyz2TVs5ehxq', 'user 7', 'user');
INSERT INTO be_users(id,phone_number, email, password, name, role) VALUES ('F0C8CB35F1434C8BABE8E736E35A4A0E','000000010', 'minhnhat.kd.hungyen8@gmail.com', '$2a$10$aI1HrbKMlpUI/aheMG.o7.Ygu7DVPHtEDdgXc.G.suyz2TVs5ehxq', 'user 8', 'user');
INSERT INTO be_users(id,phone_number, email, password, name, role) VALUES ('F0C8CB35F1434C8BABE8E736E37A4A0E','000000011', 'tobiedax@gmail.com', '$2a$10$aI1HrbKMlpUI/aheMG.o7.Ygu7DVPHtEDdgXc.G.suyz2TVs5ehxq', 'user 9', 'user');


CREATE TABLE be_tokens (
    id NVARCHAR2 (255) NOT NULL PRIMARY KEY,
    token VARCHAR2 (500) UNIQUE NOT NULL,
    token_type VARCHAR2 (255) NOT NULL,
    expiration_date DATE,
    revoked NUMBER (1) NOT NULL,
    expired NUMBER (1) NOT NULL,
    refresh_token VARCHAR2 (255) NOT NULL,
    refresh_expiration_date DATE,
    user_id NVARCHAR2 (255),
    CONSTRAINT fk_user_id_be_tokens FOREIGN KEY (user_id) REFERENCES be_users (id)
);
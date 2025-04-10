-- 创建枚举类型
CREATE TYPE equipment_type AS ENUM ('Compressor', 'Turbine', 'Pump');

-- 创建 Equipments 表
CREATE TABLE Equipments (
                            id SERIAL PRIMARY KEY,          -- 自增主键
                            location VARCHAR(255),          -- 设备位置，字符串类型
                            type equipment_type NOT NULL    -- 设备类型，使用枚举类型
);

ALTER TABLE Equipments ADD COLUMN faulty BOOLEAN DEFAULT FALSE;

CREATE TABLE Users (
                       id SERIAL PRIMARY KEY,          -- 自增主键
                       username VARCHAR(50) NOT NULL,  -- 用户名，限制为 50 个字符
                       password VARCHAR(100) NOT NULL, -- 密码，限制为 100 个字符（适合存储哈希值）
                       location VARCHAR(150)           -- 位置，限制为 150 个字符
);

-- 创建 FaultyRecords 表
CREATE TABLE FaultyRecords (
                               id SERIAL PRIMARY KEY,                  -- 自增主键
                               equipmentId INTEGER NOT NULL,          -- 设备 ID，非空
                               time TIMESTAMP NOT NULL,               -- 故障时间，非空
                               CONSTRAINT fk_equipment FOREIGN KEY (equipmentId) REFERENCES Equipments(id)
                                   ON DELETE CASCADE                  -- 外键约束，级联删除
);

-- 创建 MaintainRecords 表
CREATE TABLE MaintainRecords (
                                 id SERIAL PRIMARY KEY,                  -- 自增主键
                                 operatorId INTEGER NOT NULL,           -- 操作员 ID，非空
                                 time TIMESTAMP NOT NULL,               -- 维护时间，非空
                                 CONSTRAINT fk_operator FOREIGN KEY (operatorId) REFERENCES Users(id)
                                     ON DELETE CASCADE                  -- 外键约束，级联删除
);

ALTER TABLE Users ADD CONSTRAINT unique_username UNIQUE (username);
ALTER TABLE admins ADD CONSTRAINT unique_username UNIQUE (username);
ALTER TABLE maintainrecords ADD COLUMN user_type VARCHAR(15);

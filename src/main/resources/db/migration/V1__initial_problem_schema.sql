CREATE TABLE problems (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(255) NOT NULL,
    online_judge VARCHAR(255) NOT NULL,
    fetching_status VARCHAR(255) NOT NULL,
    title VARCHAR(255),
    contest_name VARCHAR(255),
    problem_url VARCHAR(255),
    contest_url VARCHAR(255),
    solved_count INT DEFAULT 0,
    extra_metadata JSON,
    created_date TIMESTAMP NOT NULL,
    last_modified_date TIMESTAMP NOT NULL,
    created_by VARCHAR(255),
    modified_by VARCHAR(255),
    UNIQUE (code, online_judge),
    INDEX idx_code_online_judge (code, online_judge)
);

CREATE TABLE property (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255),
    content LONGTEXT,
    problem_id BIGINT NOT NULL,
    created_date TIMESTAMP NOT NULL,
    last_modified_date TIMESTAMP NOT NULL,
    created_by VARCHAR(255),
    modified_by VARCHAR(255),
    FOREIGN KEY (problem_id) REFERENCES problems(id)
);

CREATE TABLE sample_test_cases (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    input TEXT NOT NULL,
    output TEXT NOT NULL,
    problem_id BIGINT NOT NULL,
    sample_order INT NOT NULL,
    created_date TIMESTAMP NOT NULL,
    last_modified_date TIMESTAMP NOT NULL,
    created_by VARCHAR(255),
    modified_by VARCHAR(255),
    FOREIGN KEY (problem_id) REFERENCES problems(id)
);

CREATE TABLE section (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    problem_id BIGINT NOT NULL,
    title VARCHAR(255),
    section_format VARCHAR(255),
    content LONGTEXT,
    section_order INT,
    created_date TIMESTAMP NOT NULL,
    last_modified_date TIMESTAMP NOT NULL,
    created_by VARCHAR(255),
    modified_by VARCHAR(255),
    FOREIGN KEY (problem_id) REFERENCES problems(id)
);
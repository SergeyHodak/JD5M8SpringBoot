CREATE TABLE user_address (
    email VARCHAR(255) PRIMARY KEY,
    address VARCHAR(255)
);

INSERT INTO user_address (email, address) VALUES ('john.doe1@mail.com', 'New York City');
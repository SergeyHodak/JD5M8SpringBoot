ALTER TABLE "user" ADD COLUMN password VARCHAR(200);
ALTER TABLE "user" ADD COLUMN authority VARCHAR(100);

INSERT INTO "user" (email, password, authority)
VALUES ('user@mail.com', '{noop}user_password', 'USER'),
       ('admin@mail.com', '{bcrypt}$2a$10$epG9qWLVKfZe/G8wY6j6kO4s7ZbTkgW.TLUgaKNqDBYInzopjeIx2', 'admin, user');
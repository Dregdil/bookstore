INSERT INTO "user" (email,login, password) VALUES
        ('john@example.com', 'john123', '{noop}password123');
INSERT INTO "user" (email, authorities, login, password) VALUES
    ('admin@example.com','ROLE_ADMIN', 'admin', '{noop}admin');


INSERT INTO basket (user_id) VALUES
    (1),
    (2);

INSERT INTO book (title, author, price) VALUES ('To Kill a Mockingbird', 'Harper Lee', 9.99);
INSERT INTO book (title, author, price) VALUES ('1984', 'George Orwell', 12.99);
INSERT INTO book (title, author, price) VALUES ('Pride and Prejudice', 'Jane Austen', 8.99);
INSERT INTO book (title, author, price) VALUES ('The Great Gatsby', 'F. Scott Fitzgerald', 10.99);
INSERT INTO book (title, author, price) VALUES ('Moby-Dick', 'Herman Melville', 11.99);
INSERT INTO book (title, author, price) VALUES ('To the Lighthouse', 'Virginia Woolf', 9.99);
INSERT INTO book (title, author, price) VALUES ('The Catcher in the Rye', 'J.D. Salinger', 13.99);
INSERT INTO book (title, author, price) VALUES ('Brave New World', 'Aldous Huxley', 10.99);
INSERT INTO book (title, author, price) VALUES ('The Hobbit', 'J.R.R. Tolkien', 11.99);
INSERT INTO book (title, author, price) VALUES ('Jane Eyre', 'Charlotte Bronte', 9.99);
INSERT INTO book (title, author, price) VALUES ('The Lord of the Rings', 'J.R.R. Tolkien', 14.99);
INSERT INTO book (title, author, price) VALUES ('The Grapes of Wrath', 'John Steinbeck', 12.99);
INSERT INTO book (title, author, price) VALUES ('Wuthering Heights', 'Emily Bronte', 9.99);
INSERT INTO book (title, author, price) VALUES ('The Odyssey', 'Homer', 10.99);
INSERT INTO book (title, author, price) VALUES ('Frankenstein', 'Mary Shelley', 8.99);
INSERT INTO book (title, author, price) VALUES ('Crime and Punishment', 'Fyodor Dostoevsky', 11.99);
INSERT INTO book (title, author, price) VALUES ('The Picture of Dorian Gray', 'Oscar Wilde', 9.99);
INSERT INTO book (title, author, price) VALUES ('The Adventures of Huckleberry Finn', 'Mark Twain', 12.99);
INSERT INTO book (title, author, price) VALUES ('Les Misérables', 'Victor Hugo', 10.99);
INSERT INTO book (title, author, price) VALUES ('The Scarlet Letter', 'Nathaniel Hawthorne', 8.99);
INSERT INTO book (title, author, price) VALUES ('One Hundred Years of Solitude', 'Gabriel Garcia Marquez', 11.99);
INSERT INTO book (title, author, price) VALUES ('The Brothers Karamazov', 'Fyodor Dostoevsky', 9.99);
INSERT INTO book (title, author, price) VALUES ('The Count of Monte Cristo', 'Alexandre Dumas', 14.99);
INSERT INTO book (title, author, price) VALUES ('Anna Karenina', 'Leo Tolstoy', 12.99);
INSERT INTO book (title, author, price) VALUES ('Catch-22', 'Joseph Heller', 10.99);


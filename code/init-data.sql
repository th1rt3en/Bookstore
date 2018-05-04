DELETE FROM book;
ALTER TABLE book AUTO_INCREMENT = 1;
INSERT IGNORE INTO book (Title, Author, ISBN, Price, In_Stock, Category) VALUES 
("The Shadows over Innsmouth", "HP Lovecraft", "HPL001", 50.0, 5, "Horror"),
("Winged Death", "HP Lovecraft", "HPL002", 70.0, 4, "Horror"),
("The Haunter of the Dark", "HP Lovecraft", "HPL003", 35.0, 1, "Horror"),
("The Fellowship of the Ring", "JRR Tolkien", "JRRT001", 80.0, 1, "Fantasy"),
("The Two Towers", "JRR Tolkien", "JRRT002", 75.0, 8, "Fantasy"),
("Concepts of Programming Languages", "Robert W. Sebesta", "RWS001", 55.0, 9, "Science"),
("Computer Organization and Design", "David Patterson", "DP001", 15.75, 4, "Science")
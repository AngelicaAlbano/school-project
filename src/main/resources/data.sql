insert into User (username, email)
values ('alex', 'alex@email.com');
insert into User (username, email)
values ('ana', 'ana@email.com');

insert into Course (code, name, description)
values ('java-1', 'Java OO', 'Java and Object Orientation: Encapsulation, Inheritance and Polymorphism.');
insert into Course (code, name, description)
values ('java-2', 'Java Collections', 'Java Collections: Lists, Sets, Maps and more.');

insert into ENROLLMENT(USER_ID, registerDate, courseCode)
values (2, '1994-04-19 07:00:00', 'java-1');
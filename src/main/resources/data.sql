insert into User (username, email)
values ('alex', 'alex@email.com');
insert into User (username, email)
values ('ana', 'ana@email.com');

insert into Course (code, name, description)
values ('java-1', 'Java OO', 'Java and Object Orientation: Encapsulation, Inheritance and Polymorphism.');
insert into Course (code, name, description)
values ('java-2', 'Java Collections', 'Java Collections: Lists, Sets, Maps and more.');

insert into Enrollment(username, registerDate, coursecode)
values ('ana', '2023-03-01', 'java-1');
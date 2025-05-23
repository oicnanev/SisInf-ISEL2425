CREATE TABLE country (
	countryId serial primary key,
	name varchar(50) not null unique
);

CREATE TABLE student (
	studentNumber serial primary key,
	name varchar(50) not null,
    sex varchar(1) not null,
    dateBirth date not null,
    country int REFERENCES country(countryId)
);

INSERT INTO country(name) 
VALUES ('Portugal'), ('Spain'), ('Ireland'), ('Greece');

INSERT INTO student(studentNumber, name, dateBirth, sex, country)
VALUES (123, 'Bob', '1970-01-01', 'M', 1), 
       (124, 'Alice', '1971-01-10', 'F', 2), 
       (999, 'Carol', '1990-02-14', 'F', 3);

CREATE TABLE course (
    courseId serial primary key,
    name     varchar(256) not null
);

CREATE TABLE studentCourse (
    studentId int references public.Student,
    courseId  int references public.Course,
    primary key (studentId, courseId)
);

INSERT INTO course(name) 
VALUES ('Sistemas de Informação'),
       ('Programação Concorrente'),
       ('Comunicação Digital');

INSERT INTO studentCourse(studentNumber, name, dateBirth, sex, country)
VALUES (123, 1), (124, 2), (999, 3), (123, 2), (123, 3);

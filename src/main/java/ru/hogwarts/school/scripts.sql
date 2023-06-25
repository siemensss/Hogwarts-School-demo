/*получение всех студентов*/
SELECT * FROM student;


/*возраст между 10 и 20*/
SELECT * FROM student
WHERE age > 10 AND age <20;


/*список имен всех студентов*/
SELECT name FROM student;


/*в имени есть буква "о"*/
SELECT * FROM student
WHERE name LIKE '%o%';


/*возраст меньше идентификатора*/
SELECT * FROM student
WHERE age < id;


/*студенты упорядочены по возрасту*/
SELECT * FROM student
ORDER BY age;


/*связь таблиц, все студенты красного факультета*/
SELECT st.* FROM student AS st, faculty AS f
WHERE st.faculty_id = f.id
  AND f.color = 'Red';
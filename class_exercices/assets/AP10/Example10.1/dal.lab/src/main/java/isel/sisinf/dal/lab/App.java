/*
MIT License

Copyright (c) 2022-2024, Nuno Datia, João Vitorino, ISEL

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package isel.sisinf.dal.lab;

/*

  Didactic material to support
  the Information Systems course

  The examples may not be complete and/or totally correct.
  They are made available for teaching purposes and
  Any inaccuracies are the subject of discussion in classes.


   Application-managed Entity Manager, com Local Transaction

  */

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class App {
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("dal-lab");
        EntityManager em = emf.createEntityManager();
        try {
            System.out.println("Students");
            System.out.println("------------------------");
            // 1) Lets fetch all users! hmm, something is missing in the query...
            var query = em.createQuery("SELECT s FROM Student s", Student.class); // psst, this is JPQL What should the type be?
            // 2) Oh no! We know each user as one country, but we cant see it...
            var students = query.getResultList();
            // students.forEach(System.out::println);  // Código original
            students.forEach(s -> {
                // Acessa Country dentro do contexto ativo do EntityManager
                System.out.println("Student: " + s + ", Country: " + s.getCountry());
            });

            // Students not in Portugal
            studentsNotInPortugal(em);

            // Youngest Student
            youngestStudent(em);

            // Student with number 123
            getStudentByNumber(em, 123);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    private static void studentsNotInPortugal(EntityManager em) {
        System.out.println();
        System.out.println("Students not in Portugal");
        System.out.println("------------------------");
        var query = em.createQuery("SELECT s FROM Student s WHERE s.country.name <> 'Portugal'", Student.class);
        var students = query.getResultList();
        students.forEach(s -> {
            System.out.println("Student: " + s + ", Country: " + s.getCountry());
        });
    }

    private static void youngestStudent(EntityManager em) {
        System.out.println();
        System.out.println("Youngest Student");
        System.out.println("------------------------");
        var query = em.createQuery("SELECT s FROM Student s ORDER BY s.dateBirth DESC", Student.class).setMaxResults(1);
        var students = query.getResultList();
        students.forEach(s -> {
            System.out.println("Student: " + s + ", Country: " + s.getCountry());
        });
    }

    private static void getStudentByNumber(EntityManager em, int number) {
        System.out.println();
        System.out.println("Get Student by Number");
        System.out.println("------------------------");
        Student student = em.find(Student.class, number);
        /* or
        em.createQuery("SELECT s FROM Student s WHERE s.studentNumber = :number", Student.class)
                .setParameter("number", 123);
         */
        System.out.println("Student: " + student + ", Country: " + student.getCountry());
    }
}

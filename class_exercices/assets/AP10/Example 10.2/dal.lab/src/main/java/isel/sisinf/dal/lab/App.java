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
            /* Point 1
                Let's begin by creating a new student!
                If you paid attention to the data model you can see that a student must have a country.
                Note as well that we will begin a transaction!

                Answer questions and then move to the end of Point 2 and stop there (e.g., with a breakpoint)
            */
            System.out.println("--# CREATE Student");
            //em.getTransaction().begin();

            Country cn = new Country();
            cn.setName("France");

            /* Point 2

                Answer questions and then move to the end of Point 3 and stop there (e.g., with a breakpoint)
             */
            Student st = new Student(456, "Chiquinho", new java.util.Date(), 'M', cn);
            System.out.println("Student: " + st);
            System.out.println("Country.countryId: " + cn.getCountryId());
            
            /* Point 3

                Answer questions and then move to the end of Point 4 and stop there (e.g., with a breakpoint)
            */
            em.persist(cn);
            System.out.println("Student: " + st);
            System.out.println("Country.countryId: " + cn.getCountryId());
            
            /* Point 4

                Answer questions and then move to the end of Point 5 and stop there (e.g., with a breakpoint)
            */
            System.out.println("Country.countryId: " + cn); //check ID of homeCountry
            //em.flush();
            System.out.println("Country.countryId: " + cn); //check ID of homeCountry

            em.persist(st);
            //em.getTransaction().commit();
            System.out.println("Student: " + st); //check ID of homeCountry

           /* Point 5

                Answer questions and come back later to finish Point 6 :)
            */
            System.out.println("\n--# Insert succeeded Done!");
            

            /* Point 6
                Clean up the database in change the above line to em.getTransaction().commit() again, then uncomment the lines up until point 7

                 Answer questions and then come back later
            */

            System.out.println("\n--# Going to delete...");
            em.getTransaction().begin();
            em.remove(cn);
            em.remove(em.find(Student.class,st.getStudentNumber()));
            em.flush();
            em.getTransaction().commit();
            em.clear();
            System.out.println("\n--# Clean up!!");


            /* Point 7
                Implement the necessary classes and make the necessary/sufficient changes to make the next two code blocks work as expected.
                Don't forget to uncoment them :)
            */

            //Get student n 123
            st = em.find(Student.class,123);
            System.out.println(st); 
            for(Object c1: st.getCourses())
            {
                System.out.println(c1);
            }


            //Get course 1
            Course c = em.find(Course.class,1L);
            System.out.println(st); 
            for(Object st1: c.getStudents())
            {
                System.out.println(st1);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            em.close();
            emf.close();
        }
    }
}

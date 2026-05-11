package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.Arrays;


public class App {
  public static void main(String[] args) {

    Student s1 = new Student();
    s1.setStudent_id(2);
    s1.setName("Aarna");
    s1.setDepartment("CSE");
    s1.setEnrollment_Number("CS102");
    s1.setEmail("Aarna23@gmail.com");
    s1.setSemester(4);

    Student s2 = new Student();
    s2.setStudent_id(3);
    s2.setName("Isha");
    s2.setDepartment("CSE");
    s2.setEnrollment_Number("CS103");
    s2.setEmail("Isha653@gmail.com");
    s2.setSemester(4);

    Course c1 = new Course();
    c1.setCourse_Id("CS403");
    c1.setName("Operating System");

      Course c2 = new Course();
      c2.setCourse_Id("CS404");
      c2.setName("DBMS");

      s1.setCourses(Arrays.asList(c1,c2));
      c1.setStudents(Arrays.asList(s1,s2));
      c2.setStudents(Arrays.asList(s1));


      SessionFactory sf    = new Configuration()
              .addAnnotatedClass(Student.class)
              .addAnnotatedClass(Course.class)
               .configure()
              .buildSessionFactory();

    Session s = sf.openSession();

    Transaction tx = s.beginTransaction();

      s.persist(c1);
      s.persist(c2);
      s.persist(s1);
      s.persist(s2);
      System.out.println(s1);
     tx.commit();

    s.close();
  }
}

package ru.gb.lesson4.hw;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import ru.gb.lesson4.Person;

import java.util.List;


public class Homework {

    /**
     * 1. Создать сущность Student с полями:
     * 1.1 id - int
     * 1.2 firstName - string
     * 1.3 secondName - string
     * 1.4 age - int
     * 2. Подключить hibernate. Реализовать простые запросы: Find(by id), Persist, Merge, Remove
     * 3. Попробовать написать запрос поиска всех студентов старше 20 лет (session.createQuery)
     */

    public static void main(String[] args) {
        Configuration configuration = new Configuration().configure();
        try (SessionFactory sessionFactory = configuration.buildSessionFactory()) {
            insertStudent(sessionFactory);
            findById(sessionFactory, 3L);
            findById(sessionFactory, 10L);
            findById(sessionFactory, 11L);
            mergeStudent(sessionFactory, 3L);
            findById(sessionFactory, 3L);
            removeStudent(sessionFactory, 10L);
            findById(sessionFactory, 10L);
            findByAge(sessionFactory, 20L);
        }
    }

    private static void findByAge(SessionFactory sessionFactory, long age) {
        try(Session session = sessionFactory.openSession()){
            Query<Student> query = session.createQuery("from Student where age > :age", Student.class);
            query.setParameter("age",age);
            List<Student> resultList = query.getResultList();
            for(Student student: resultList){
                System.out.println(student);
            }
        }
    }

    private static void insertStudent(SessionFactory sessionFactory) {
        try(Session session = sessionFactory.openSession()){
            Transaction tx = session.beginTransaction();
            for (long i = 1; i <= 10; i++) {
                Student student = new Student();
                student.setId(i);
                student.setFirstName("Student # " + i);
                student.setSecondName("Student # " + i);
                student.setAge(17 + i);
                session.persist(student);
            }
            tx.commit();
        }
    }
    private static Student findById(SessionFactory sessionFactory, long id) {
        Student student;
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            student = session.find(Student.class, id);
            System.out.println(student);
            tx.commit();
        }
        return student;
    }

    private static void mergeStudent(SessionFactory sessionFactory, long id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Student student = findById(sessionFactory, id);
            student.setSecondName("Popov");
            session.merge(student);
            tx.commit();
        }
    }
    private static void removeStudent(SessionFactory sessionFactory, long id){
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Student student = session.find(Student.class, id);
            session.remove(student);
            tx.commit();
        }
    }
}

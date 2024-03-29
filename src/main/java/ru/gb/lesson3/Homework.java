package ru.gb.lesson3;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import oracle.jdbc.pool.OracleDataSource;

/**
 * 0. Разобрать код с семниара
 * 1. Повторить код с семниара без подглядываний на таблице Student с полями:
 * 1.1 id - int
 * 1.2 firstName - string
 * 1.3 secondName - string
 * 1.4 age - int
 * 2.* Попробовать подключиться к другой БД
 * 3.** Придумать, как подружить запросы и reflection:
 * 3.1 Создать аннотации Table, Id, Column
 * 3.2 Создать класс, у которого есть методы:
 * 3.2.1 save(Object obj) сохраняет объект в БД
 * 3.2.2 update(Object obj) обновляет объект в БД
 * 3.2.3 Попробовать объединить save и update (сначала select, потом update или insert)
 */

public class Homework {
    public static void main(String[] args) {
        //String url =  "jdbc:postgresql://hostname:port/dbname";
        String url = "jdbc:h2:mem:test";
        try (Connection connection = DriverManager.getConnection(url)){
            List<Student> studentsList = new ArrayList<>();
            acceptConnection(connection, studentsList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static void acceptConnection(Connection connection, List<Student> studentsList){
        createTable(connection);
        insertData(connection);
        deleteRowById(connection, 3L);
        updateRow(connection, "Olga", "Titova", "Perova");

        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("select id, firstName, secondName, age from student");
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String firstName = resultSet.getString("firstName");
                String secondName = resultSet.getString("secondName");
                long age = resultSet.getLong("age");
                studentsList.add(new Student(id, firstName, secondName, age));
            }

            for(Student student: studentsList){
                System.out.println(student);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static void createTable(Connection connection){
        try (Statement statement = connection.createStatement()){
            statement.execute("""
      create table student (
        id bigint,
        firstName varchar(256),
        secondName varchar(256),
        age bigint
      )
      """);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static void insertData(Connection connection){
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("""
        insert into student(id, firstName, secondName, age) values
        (1, 'Igor', 'Popov', 36), 
        (2, 'Olga', 'Titova', 27), 
        (3, 'Ira', 'Letova', 45), 
        (4, 'Roma', 'Sokolov', 52), 
        (5, 'Peter', 'Ulov', 33) 
        """);
        } catch (SQLException e) {
        throw new RuntimeException(e);
        }
    }

    private static void deleteRowById(Connection connection, long id){
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("delete from student where id = " + id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void updateRow(Connection connection, String firstName, String secondName, String newSecondName) {
        try (PreparedStatement stmt = connection.prepareStatement("update student set secondName = $1 where firstName = $2 and secondName = $3")) {
            stmt.setString(1, newSecondName);
            stmt.setString(2, firstName);
            stmt.setString(3, secondName);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


  /*
  @Table(name = "person")
  static class Person {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;

    public int getId() {
      return id;
    }

    public void setId(int id) {
      this.id = id;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
  }
  */

}

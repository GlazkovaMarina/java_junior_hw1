package ru.gb.lesson1.hw;

import ru.gb.lesson1.Streams;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class Homework {

  /**
   * Реалзиовать методы, описанные ниже:
   */

  /**
   * Вывести на консоль отсортированные (по алфавиту) имена персонов
   */
  public void printNamesOrdered(List<Streams.Person> persons) {
    persons.stream().map(Streams.Person::getName).sorted().forEach(System.out::println);
  }

  /**
   * В каждом департаменте найти самого взрослого сотрудника.
   * Вывести на консоль мапипнг department -> personName
   * Map<Department, Person>
   */
  public Map<Streams.Department, Streams.Person> printDepartmentOldestPerson(List<Streams.Person> persons) {
    throw new UnsupportedOperationException();
  }

  /**
   * Найти 10 первых сотрудников, младше 30 лет, у которых зарплата выше 50_000
   */
  public List<Streams.Person> findFirstPersons(List<Streams.Person> persons) {
    throw new UnsupportedOperationException();
  }

  /**
   * Найти депаратмент, чья суммарная зарплата всех сотрудников максимальна
   */
  public Optional<Streams.Department> findTopDepartment(List<Streams.Person> persons) {
    throw new UnsupportedOperationException();
  }

}

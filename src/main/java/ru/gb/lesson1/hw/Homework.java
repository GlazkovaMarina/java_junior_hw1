package ru.gb.lesson1.hw;

import ru.gb.lesson1.Streams;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    Comparator<Streams.Person> ageComparator = Comparator.comparing(Streams.Person::getAge);

    return persons.stream().collect(Collectors.toMap(it -> it.getDepartment(), Function.identity(), (first, second) -> {
      if (ageComparator.compare(first, second) > 0) {
        return first;
      }
      return second;
    }));
  }

  /**
   * Найти 10 первых сотрудников, младше 30 лет, у которых зарплата выше 50_000
   */
  public List<Streams.Person> findFirstPersons(List<Streams.Person> persons) {
    return persons.stream().filter(person -> person.getAge() < 30).filter(person -> person.getSalary() > 50_000).limit(10).toList();
  }

  /**
   * Найти депаратмент, чья суммарная зарплата всех сотрудников максимальна
   */
  public Optional<Streams.Department> findTopDepartment(List<Streams.Person> persons) {

    return persons.stream()
            .collect(Collectors.groupingBy(Streams.Person::getDepartment, Collectors.summingDouble(Streams.Person::getSalary)))
            .entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey);
  }

}

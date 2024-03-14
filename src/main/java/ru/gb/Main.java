package ru.gb;

import ru.gb.lesson1.Streams;
import ru.gb.lesson1.hw.Homework;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Main {

  public static void main(String[] args) {
    List<Streams.Department> departments = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      departments.add(new Streams.Department("Department #" + i));
    }

    List<Streams.Person> persons = new ArrayList<>();
    for (int i = 0; i < 50; i++) {
      persons.add(new Streams.Person(
              "Person #" + i,
              ThreadLocalRandom.current().nextInt(20, 61),
              ThreadLocalRandom.current().nextInt(20_000, 100_000) * 1.0,
              departments.get(ThreadLocalRandom.current().nextInt(departments.size()))
      ));
    }
      Homework homework = new Homework();
      homework.printNamesOrdered(persons);
  }

}
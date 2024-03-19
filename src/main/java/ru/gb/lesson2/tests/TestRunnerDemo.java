package ru.gb.lesson2.tests;

public class TestRunnerDemo {

  // private никому не видно
  // default (package-private) внутри пакета
  // protected внутри пакета + наследники
  // public всем

  public static void main(String[] args) {
    TestRunner.run(TestRunnerDemo.class);
  }
  @BeforeAll
  public void beforeAll(){
    System.out.println("BeforeAll");
  }
  @BeforeEach
  public void beforeEach(){
    System.out.println("BeforeEach");
  }
  @AfterEach
  public void afterEach(){
    System.out.println("AfterEach");
  }
  @AfterAll
  public void afterAll(){
    System.out.println("AfterAll");
  }

  @Test (order = 3)
  private void test1() {
    System.out.println("test1");
  }

  @Test (order = 2)
  void test2() {
    System.out.println("test2");
  }

  @Test (order = 1)
  void test3() {
    System.out.println("test3");
  }

  @Test (order = 4)
  void test4() {
    System.out.println("test4");
  }


}

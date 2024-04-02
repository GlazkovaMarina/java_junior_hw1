package ru.gb.lesson2.tests;

import java.lang.reflect.AccessFlag;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TestRunner {
  public static void run(Class<?> testClass) {
    final Object testObj = initTestObj(testClass);

    List<Method> methodList = new ArrayList<>();
    Method[] methods = testClass.getDeclaredMethods();
    for (Method testMethod : methods){
      if (testMethod.getAnnotation(Test.class) != null && !testMethod.accessFlags().contains(AccessFlag.PRIVATE)){
        methodList.add(testMethod);
      }
    }
    methodList.sort(new Comparator<Method>() {
      @Override
      public int compare(Method o1, Method o2) {
        return o1.getAnnotation(Test.class).order() - o2.getAnnotation(Test.class).order();
      }
    });

    try {
        testClass.getMethod("beforeAll").invoke(testObj);
    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
    for (Method testMethod : methodList) {
        try {
          testClass.getMethod("beforeEach").invoke(testObj);
          testMethod.invoke(testObj);
          testClass.getMethod("afterEach").invoke(testObj);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
          throw new RuntimeException(e);
        }
    }
    try {
    testClass.getMethod("afterAll").invoke(testObj);
    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }

  private static Object initTestObj(Class<?> testClass) {
    try {
      Constructor<?> noArgsConstructor = testClass.getConstructor();
      return noArgsConstructor.newInstance();
    } catch (NoSuchMethodException e) {
      throw new RuntimeException("Нет конструктора по умолчанию");
    } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
      throw new RuntimeException("Не удалось создать объект тест класса");
    }
  }

}

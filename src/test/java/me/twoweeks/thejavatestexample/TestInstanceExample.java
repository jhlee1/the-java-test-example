package me.twoweeks.thejavatestexample;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

// JUnit은 매 @Test마다 인스턴스를 생성 - 각 테스트 간의 의존성을 갖도록하지 않게 하기 위해 => 일반적으로 테스트 순서가 보장되지 않기 때문에
// 전체 테스트를 실행시키면 순서에 따라 한 곳에선 2가 찍혀야 할 것 같지만 모두 1로 찍힘
// this를 찍어보면 서로 객체 주소가 다름.

// Junit 5부터 하나의 인스턴스로 한 클래스의 전체 메소드가 공유할 수 있도록 변경 가능
// 객체 생성을 줄이게되므로 성능 향상
@TestInstance(Lifecycle.PER_CLASS) // 클래스 당 하나의 인스턴스를 갖도록 함
public class TestInstanceExample {
  int value = 1;

  @BeforeAll
  void beforeAll() { // 클래스에서 하나의 인스턴스만 사용하기 때문에 더 이상 static일 필요가 없음
    System.out.println("before all");
  }

  @AfterAll
  void afterAll() {
    System.out.println("after all");
  }

  @Test
  void test1() {
    System.out.println(this);
    System.out.println(value++);
  }


  @Test
  void test2() {
    System.out.println(this);
    System.out.println(value++);
  }




}

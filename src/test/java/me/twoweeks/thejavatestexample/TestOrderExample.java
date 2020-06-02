package me.twoweeks.thejavatestexample;

// 기본적으로 unit간에 순서에 상관없이 테스트가 가능해야함
// 서로 간에 의존성이 존재하면 각 단위로써 존재하는 의미가 사라짐
// 상황에 따라서 로그인 -> 액션 -> 로그아웃 이런 식으로 쭉 연결된 테스트가 필요할 때가 있음

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;

//@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestOrderExample {

  @Order(2)
  @Test
  void test2() {
    System.out.println("Test 2");
  }

  @Order(1)
  @Test
  void test1() {
    System.out.println("Test 1");
  }

  @Order(3)
  @Test
  void test3() {
    System.out.println("Test 3");
  }


}

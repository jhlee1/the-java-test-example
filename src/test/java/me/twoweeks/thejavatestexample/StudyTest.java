package me.twoweeks.thejavatestexample;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Created by Joohan Lee on 2020/03/27
 */
class StudyTest {

  // Reflection을 사용하기 때문에 public accessor를 넣지 않아도 괜찮음
  @Test
  void create() {
    System.out.println("created");
  }


  @Test
  @Disabled // 테스트를 돌릴 때 무시하고 지나감
  void create1() {
    System.out.println("create1");
  }

  // 모든 @Test들을 실행하기 전에 딱 한번만 사용됨. static으로 선언해야됨
  @BeforeAll
  static void beforeAll() {

  }

  //BeforeAll과 마찬가지인데 모든 테스트가 끝나고 실행됨
  @AfterAll
  static void afterAll() {

  }

  // 각각 테스트 전에 실행됨. static일 필요는 없음
  @BeforeEach
  void beforeEach() {

  }

  @AfterEach
  void afterEach() {

  }

}
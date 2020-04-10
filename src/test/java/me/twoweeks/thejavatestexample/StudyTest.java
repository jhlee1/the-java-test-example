package me.twoweeks.thejavatestexample;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.time.Duration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnJre;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.api.condition.OS;

/**
 * Created by Joohan Lee on 2020/03/27
 */
class StudyTest {

  // Reflection을 사용하기 때문에 public accessor를 넣지 않아도 괜찮음
  @Test
  @DisplayName("스터디 만들기")
  void createNewStudy() { // 위에 displayname 어노테이션을 쓰지 않을 경우 _로 보기쉽게 표기
    Study study = new Study(-10);

    assertAll( // assertAll로 묶는 경우 위에 테스트가 실패해도 아래 테스트가 실행되서 결과를 알 수 있음
        () -> assertNotNull(study),
        () -> assertEquals(StudyStatus.DRAFT, study.getStudyStatus(),
            () -> "처음 스터디를 만들면 " + StudyStatus.DRAFT + " 상태다."),
        () -> assertTrue(study.getLimit() > 0, "스터디 최대 참석 가능 인원은 1이상 이어야 한다.") // lambda 는 실패한 경우에만 실행되는데 그냥 바로 문자열로 넣어버리면 매번 실행됨
    );

    assertNotNull(study);

    System.out.println("created");
  }


  @Test
  @DisplayName("스터디 만들기 \uD83D \uDE31") //charset 변경하면 이모지도 지원가능
  void study_exception_handling() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Study(-10)); // Exception이 발생하는지 테스트
    assertEquals("Limit은 0보다 커야한다.", exception.getMessage());
  }

  @Test
  @DisplayName("스터디 Timeout") //charset 변경하면 이모지도 지원가능
  void study_timeout() {
    assertTimeout(Duration.ofMillis(10), () -> {
      new Study(10);
      Thread.sleep(300);
    }); // 문제점: 10 milliseconds가 넘어도 해당 코드블럭이 다 완료될때까지 (300 milliseconds 동안) 실행된다.

    assertTimeoutPreemptively(Duration.ofMillis(10), () -> {
      new Study(10);
      Thread.sleep(300);
    }); // 어떤 Thread에서 실행되는지 주의해야한다. Spring Transaction이 존재하는 ThreadLocal과 다른 thread에서 처리될 수도 있기 때문에 테스트가 끝나고 Rollback이 안될 수 있음
  }

  @Test
  @DisplayName("스터디 Timeout") //charset 변경하면 이모지도 지원가능
  @EnabledOnOs({OS.MAC, OS.LINUX})
//  @DisabledOnOs({OS.MAC})
  @EnabledIfEnvironmentVariable(named = "TEST_ENV", matches = "LOCAL") // 아래 assumeTrue랑 같은 기능
  @DisabledOnJre({JRE.JAVA_11, JRE.JAVA_10, JRE.JAVA_9})
  void study_on_condition() {
    String test_env = System.getenv("TEST_ENV");
    System.out.println(test_env);
    assumeTrue("LOCAL".equalsIgnoreCase(test_env));

    Study actual = new Study(10);
    assertThat(actual.getLimit(), greaterThan(0));
  }

  @Test
  @Disabled // 테스트를 돌릴 때 무시하고 지나감
  void study_to_be_ignored() {
    assertThrows(IllegalArgumentException.class, () -> new Study(-10)); //
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
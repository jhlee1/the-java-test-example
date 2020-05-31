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
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnJre;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

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



  // 태그 붙이기
  // ex) 빨리 끝나는 테스트(로컬에서 실행용)와 오래 걸리는 테스트(CI에서 실행)를 분리할 경우 @Tag("fast) / @Tag("slow")로 분리할 수 있음
  // intellij에선 테스트 설정에서 class말고 tags로 골라서 실행
  // Terminal에선 pom에서 plugin 설정으로 특정 태그만 실행할 수 있음. pom.xml의 <profiles> 참조. 테스트는 mvn test로 실행하면됨
  @Test
  @Tag("fast")
  void tag_example_fast() {
    assertThrows(IllegalArgumentException.class, () -> new Study(-10)); //
  }

  @Test
  @Tag("slow")
  void tag_example_slow() {
    assertThrows(IllegalArgumentException.class, () -> new Study(-10)); //
  }

  // 커스텀 태그
  // 여러 annotation을 조합해서 Meta annotation을 만들어줌
  // 기존 @Tag("fast")라고 쓰는 방식은 type-safe하지 않고 오타가 발생할 수 있음
  // @Test
  // @Tag("fast") 처럼 각각 태그를 따로 쓰는 것 대신 조합된 @FastTest 태그 하나로 처리
  @FastTest
  void tag_example_custom_meta_tag() {
    assertThrows(IllegalArgumentException.class, () -> new Study(-10)); //
  }

  // 테스트 반복하기
  // RepetitionInfo를 통해 여태 몇번 반복했는지, 몇번 반복해야 하는지를 알 수 있음
  @DisplayName("스터디만들기 반복")
  @RepeatedTest(value = 10, name = "{displayName}, {currentRepetition} / {totalRepetition}")
  void repeat_example(RepetitionInfo repetitionInfo) {
    System.out.println("test " + repetitionInfo.getCurrentRepetition() + " / " + repetitionInfo.getTotalRepetitions());
  }

  // 반복 테스트에서 각각 다른 Parameter를 넣어서 테스트해보기
  @ParameterizedTest
  @ValueSource(strings = {"The", "winter", "is", "coming"})
  void parameterizedTest(String message) {
    System.out.println(message);
  }

  // 반복 테스트에서 각각 다른 Parameter를 넣어서 테스트해보기 (이름 설정)
  @DisplayName("스터디만들기")
  @ParameterizedTest(name = "{index} {displayName} message={0}")
  @ValueSource(strings = {"The", "winter", "is", "coming"}) //strings 말고 longs, booleans 등 다양한 타입을 넣어줄 수 있음
  @EmptySource // 위에 Value source에 더하여 빈 문자열을 넣어줌
  @NullSource // 위에 Value source에 더하여 null을 넣어줌
  @NullAndEmptySource //위에 두개 합친 composite annotation
  void parameterizedTestWithName(String message) {
    System.out.println(message);
  }

  @DisplayName("Converter 써보기")
  @ParameterizedTest(name = "{index} {displayName} message={0}")
  @ValueSource(ints = {10, 20, 40})
  void parameterizedTestWithConverter(@ConvertWith(StudyConverter.class) Study study) {
    System.out.println(study.getLimit());
  }

  static class StudyConverter extends SimpleArgumentConverter {
    @Override
    protected Object convert(Object o, Class<?> aClass) throws ArgumentConversionException {
      assertEquals(Study.class, aClass, "Can only convert to Study");
      return new Study(Integer.parseInt(o.toString()));
    }
  }

  @DisplayName("Csv 써보기")
  @ParameterizedTest(name = "{index} {displayName} message={0}")
  @CsvSource({"10, '자바 스터디'", "20, 스프링"})
  void parameterizedTestWithCsv(Integer limit, String name) { // 위에 컨버터를 쓰는 방식은 argument 하나밖에 받지 못함
    System.out.println(new Study(limit, name));
  }

  @DisplayName("ArgumentsAccessor")
  @ParameterizedTest(name = "{index} {displayName} message={0}")
  @CsvSource({"10, '자바 스터디'", "20, 스프링"})
  void parameterizedTestWithArgumentsAccessor(ArgumentsAccessor argumentsAccessor) { // 위에 컨버터를 쓰는 방식은 argument 하나밖에 받지 못함
    Study study = new Study(argumentsAccessor.getInteger(0), argumentsAccessor.getString(1));
    System.out.println(study);
  }

  @DisplayName("Aggregator")
  @ParameterizedTest(name = "{index} {displayName} message={0}")
  @CsvSource({"10, '자바 스터디'", "20, 스프링"})
  void parameterizedTestWithAggregator(@AggregateWith(StudyAggregator.class) Study study) { // 위에 컨버터를 쓰는 방식은 argument 하나밖에 받지 못함
    System.out.println(study);
  }

  static class StudyAggregator implements ArgumentsAggregator { // 반드시 static한 inner class 이거나 public class여야 한다.

    @Override
    public Object aggregateArguments(ArgumentsAccessor argumentsAccessor,
        ParameterContext parameterContext) throws ArgumentsAggregationException {
      return new Study(argumentsAccessor.getInteger(0), argumentsAccessor.getString(1));
    }
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
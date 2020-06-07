package me.twoweeks.thejavatestexample;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;

/**
 * Created by Joohan Lee on 2020/06/07
 */

// Annotation 말고 코딩적으로 추가할 수 있음
//@ExtendWith(FindSlowTestExtension.class)
public class TestExtensionExample {

  @RegisterExtension
  static FindSlowTestConstructorExtension findSlowTestExtension = new FindSlowTestConstructorExtension(1000L);

  @SlowTest
  @Test
  void slow_test() throws InterruptedException {
    Thread.sleep(1005L);

  }
}

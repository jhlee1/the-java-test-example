package me.twoweeks.thejavatestexample;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;

/**
 * Created by Joohan Lee on 2020/06/07
 */
public class FindSlowTestConstructorExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback {

  private long threshold;

  public FindSlowTestConstructorExtension(long threshold) {
    this.threshold = threshold;
  }

  @Override
  public void beforeTestExecution(ExtensionContext extensionContext) throws Exception {
    String testClassName = extensionContext.getRequiredTestClass().getName();
    String testMethodName = extensionContext.getRequiredTestMethod().getName();

    ExtensionContext.Store store = extensionContext.getStore(Namespace.create(testClassName, testMethodName));

    store.put("START_TIME", System.currentTimeMillis());
  }

  @Override
  public void afterTestExecution(ExtensionContext extensionContext) throws Exception {
    String testClassName = extensionContext.getRequiredTestClass().getName();
    String testMethodName = extensionContext.getRequiredTestMethod().getName();
    ExtensionContext.Store store = extensionContext.getStore(Namespace.create(testClassName, testMethodName));

    SlowTest slowTestAnnotation = extensionContext.getRequiredTestMethod().getAnnotation(SlowTest.class);

    long startTime = store.remove("START_TIME", long.class);
    long duration = System.currentTimeMillis() - startTime;

    if (duration > threshold && slowTestAnnotation == null) {
      System.out.printf("Please consider mark method [%s] with @SlowTest.]\n", testMethodName);
    }
  }
}

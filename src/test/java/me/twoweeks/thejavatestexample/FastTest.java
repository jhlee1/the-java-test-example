package me.twoweeks.thejavatestexample;

// 커스텀 태그 annotation 만들기
// 여러 annotation을 조합해서 Meta annotation을 만들어줌

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Target(ElementType.METHOD) //Method에 쓸 수 있음
@Retention(RetentionPolicy.RUNTIME) // Runtime까지 적용됨
@Test
@Tag("fast")
public @interface FastTest {

}

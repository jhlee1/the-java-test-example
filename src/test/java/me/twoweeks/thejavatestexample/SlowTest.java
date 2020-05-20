package me.twoweeks.thejavatestexample;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Target(ElementType.METHOD) //Method에 쓸 수 있음
@Retention(RetentionPolicy.RUNTIME) // Runtime까지 적용됨
@Test
@Tag("slow")
public @interface SlowTest {

}

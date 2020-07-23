package my.alkarps.engine.helper.valid;

import my.alkarps.annotation.BeforeEach;
import my.alkarps.annotation.Test;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author alkarps
 * create date 17.07.2020 18:27
 */
public class TestClassWithTestAndBeforeEachMethods {
    @BeforeEach
    public void setUp() {
        System.out.println("setUp");
    }

    @BeforeEach
    private void setUp1() {
        System.out.println("setUp1");
    }

    @BeforeEach
    void setUp2() {
        System.out.println("setUp2");
    }

    @BeforeEach
    private void setUp3() {
        System.out.println("setUp3");
    }

    @Test
    public void publicMethod() {
        System.out.println("this public method");
    }

    @Test
    private void privateMethod() {
        fail("Вызван метод, хотя не должен");
    }

    @Test
    protected void protectedMethod() {
        System.out.println("this protected method");
    }

    @Test
    void packageMethod() {
        System.out.println("this package protected method");
    }
}
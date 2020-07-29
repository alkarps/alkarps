package my.alkarps.engine.helper.invalid;

import my.alkarps.annotation.BeforeEach;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author alkarps
 * create date 17.07.2020 18:10
 */
public class NotValidBeforeEachMethods {
    @BeforeEach
    public static void setBeforeEach(){
        fail("Вызван метод, хотя не должен");
    }
}

package my.alkarps;

import com.google.common.collect.ContiguousSet;
import com.google.common.math.IntMath;

import java.math.RoundingMode;
import java.util.stream.Collectors;

import static com.google.common.collect.DiscreteDomain.integers;
import static com.google.common.collect.Range.closed;

public class HelloOtus {
    public static void main(String... args) {
        String result = ContiguousSet.create(closed(1, 100), integers())
                .stream()
                .map(HelloOtus::map)
                .map(Object::toString)
                .collect(Collectors.joining(", "));
        System.out.println(result);
    }

    private static Integer map(Integer value) {
        if (IntMath.mod(value, 2) == 0) {
            return IntMath.log2(value, RoundingMode.HALF_EVEN);
        } else {
            return IntMath.checkedMultiply(value, value);
        }
    }
}

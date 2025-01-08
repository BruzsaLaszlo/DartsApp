package bruzsa.laszlo.dartsapp.model.x01;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

class VersionTest {

    @Test
    void versionTest() {
        System.out.println("java.version: " +
                System.getProperty("java.version"));
        System.out.println("java.runtime.version: " +
                System.getProperty("java.runtime.version"));
        System.out.println("java.vm.version: " +
                System.getProperty("java.vm.version"));
        System.out.println("java.specification.version: " +
                System.getProperty("java.specification.version"));
        System.out.println("java.vm.specification.version: " +
                System.getProperty("java.vm.specification.version"));
        class Z {
        }
        class A extends Z {
        }
        class B extends A {
        }
        class C extends B {
        }


        List<Integer> integers = new ArrayList<>();
        integers.add(1);

        List<? extends Number> numbers = integers;
        System.out.println(numbers);

        OptionalInt reduce = IntStream.of(1, 2, 3, 4)
                .reduce(Integer::sum);
        assertThat(reduce).isPresent().hasValue(10);

        int reduce100 = IntStream.of(1, 2, 3, 4)
                .reduce(100, Integer::sum);
        assertThat(reduce100).isEqualTo(110);

        int reduceM = IntStream.of(1, 2, 3, 4)
                .reduce(100, Integer::sum);

        List<String> c = List.of("ize", "Mize");
        Integer reduce1 = c.stream().reduce(0, (integer, s) -> integer + s.length(), Integer::sum);
    }
}

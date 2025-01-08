package bruzsa.laszlo.dartsapp.junit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.stream.IntStream;

class JavaTest {

    @Test
    void stream() {
        var collect = IntStream.range(0, 10000)
                .parallel()
                .collect(
                        HashMap<Integer, Integer>::new,
                        (map, value) -> map.compute(
                                value % 2,
                                (k, v) -> v == null ? 1 : v + 1),
                        (map1, map2) -> {
                            map2.forEach((key, v2) -> {
                                map1.computeIfPresent(key, (integer, v1) -> v1 + v2);
                                map1.putIfAbsent(key, v2);
                            });
                        }
                );
        assertEquals(5000, collect.get(0));
        assertEquals(5000, collect.get(1));
        assertEquals(2, collect.size());
        assertEquals(collect.get(0), collect.get(1));


        var collect2 = IntStream.range(0, 10000)
//                .parallel() !!!
                .collect(
                        HashMap<Integer, Integer>::new,
                        (map, value) -> map.compute(
                                value % 2,
                                (k, v) -> v == null ? 1 : v + 1),
                        HashMap::putAll);

        assertEquals(5000, collect2.get(0));
        assertEquals(5000, collect2.get(1));
        assertEquals(2, collect2.size());
        assertEquals(collect2.get(0), collect2.get(1));
    }

    @Test
    void Test() {
        IntStream stream = createAStreamAndPerformSomeSideEffectWithPeek();
        System.out.println("Second. I should be the second group of prints");
        consumeTheStream(stream);
    }

    IntStream createAStreamAndPerformSomeSideEffectWithPeek() {
        return IntStream.of(1, 2, 3)
                .peek(number -> System.out.println(String.format("First. My number is %d", number)))
                .map(number -> number + 1);
    }

    void consumeTheStream(IntStream stream) {
        stream.filter(number -> number % 2 == 0)
                .forEach(number -> System.out.println(String.format("Third. My number is %d", number)));
    }
}

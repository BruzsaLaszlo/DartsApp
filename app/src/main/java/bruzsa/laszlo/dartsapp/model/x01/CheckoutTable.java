package bruzsa.laszlo.dartsapp.model.x01;

import static java.lang.Integer.valueOf;
import static java.util.Collections.unmodifiableMap;

import java.util.HashMap;
import java.util.Map;

public class CheckoutTable {

    private static final String CHECKOUTS = """
            60=20 D20
            61=T15 D8
            62=T10 D16
            63=T13 D12
            64=T16 D8
            65=T19 D4
            66=T10 D18
            67=T17 D8
            68=T20 D4
            69=T15 D12
            70=T10 D20
            71=T13 D16
            72=T16 D12
            73=T19 D8
            74=T14 D16
            75=T17 D12
            76=T20 D8
            77=T19 D10
            78=T18 D12
            79=T13 D20
            80=T20 D10
            81=T19 D12
            82=T14 D20
            83=T17 D16
            84=T20 D12
            85=T15 D20
            86=T18 D16
            87=T17 D18
            88=T16 D20
            89=T19 D16
            90=T20 D15
            91=T17 D20
            92=T20 D16
            93=T19 D18
            94=T18 D20
            95=T19 D19
            96=T20 D18
            97=T19 D20
            98=T20 D19
            99=T19 10 D16
            100=T20 D20
            101=T20 1 D20
            102=T20 10 D16
            103=T20 3 D20
            104=T18 18 D16
            105=T19 16 D16
            106=T20 14 D16
            107=T19 18 D16
            108=T20 16 D16
            109=T19 20 D16
            110=T20 18 D16
            111=T20 19 D16
            112=T20 12 D20
            113=T20 13 D20
            114=T20 14 D20
            115=T20 15 D20
            116=T20 16 D20
            117=T20 17 D20
            118=T20 18 D20
            119=T19 T10 D16
            120=T20 20 D20
            121=T17 T10 D20
            122=T18 T20 D4
            123=T19 T16 D9
            124=T20 T16 D8
            125=25 T20 D20
            126=T19 T19 D6
            127=T20 T17 D8
            128=T18 T14 D16
            129=T19 T16 D12
            130=T20 20 Bull
            131=T20 T13 D16
            132=T20 T16 D12
            133=T20 T19 D8
            134=T20 T14 D16
            135=T20 T17 D12
            136=T20 T20 D8
            137=T19 T16 D16
            138=T20 T18 D12
            139=T19 T14 D20
            140=T20 T16 D16
            141=T20 T19 D12
            142=T20 T14 D20
            143=T20 T17 D16
            144=T20 T20 D12
            145=T20 T15 D20
            146=T20 T18 D16
            147=T20 T17 D18
            148=T20 T16 D20
            149=T20 T19 D16
            150=T20 T18 D18
            151=T20 T17 D20
            152=T20 T20 D16
            153=T20 T19 D18
            154=T20 T18 D20
            155=T20 T19 D19
            156=T20 T20 D18
            157=T20 T19 D20
            158=T20 T20 D19
            160=T20 T20 D20
            161=T20 T17 Bull
            164=T20 T18 Bull
            167=T20 T19 Bull
            170=T20 T20 Bull""";

    private static final Map<Integer, String> map;

    static {
        map = new HashMap<>();
        String[] lines = CHECKOUTS.split("\n");

        for (String line : lines) {
            String[] split = line.split("=");
            map.put(valueOf(split[0]), split[1]);
        }
    }

    public static String getCheckoutFor(int number) {
        return map.getOrDefault(number, "");
    }

    public static Map<Integer, String> getCheckoutMap() {
        return unmodifiableMap(map);
    }
}

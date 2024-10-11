package java_common;

import org.hellgren.utilities.unit_converter.MyUnitConverter;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

 class TestCommon {

    @Test
     void test() {
        assertEquals(1, MyUnitConverter.w2kw.apply(1000d));
    }
}

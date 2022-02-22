/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util.holiday;

import org.junit.jupiter.api.Test;


/**
 * HolidaysProviderTest.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2022/02/22 umjammer initial version <br>
 */
class HolidaysProviderTest {

    @Test
    void test() {
        HolidaysProvider provider = HolidaysProvider.defaultProvider();
provider.holydays().stream().sorted().forEach(System.err::println);
    }

    @Test
    void test2() {
        HolidaysProvider provider = new GoogleCalendarHolidaysJaProvider();
provider.holydays().stream().sorted().forEach(System.err::println);
    }
}

/* */

package io.pumpkinz.pumpkinreader;


import org.junit.Test;
import static org.junit.Assert.assertEquals;

import io.pumpkinz.pumpkinreader.util.Util;

public class UtilTest {

    @Test
    public void getDomainName() {
        assertEquals("reddit.comg", Util.getDomainName("https://www.reddit.com/r/funny/comments/3cz30y/one_of_the_better_things_ive_seen_at_comic_con/"));
    }

}

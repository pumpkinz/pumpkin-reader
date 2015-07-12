package io.pumpkinz.pumpkinreader;


import org.junit.Test;

import io.pumpkinz.pumpkinreader.util.Util;

import static org.junit.Assert.assertEquals;


public class UtilTest {

    @Test
    public void getDomainName() {
        assertEquals("reddit.com", Util.getDomainName("https://www.reddit.com/r/funny/comments/3cz30y/one_of_the_better_things_ive_seen_at_comic_con/"));
    }

}

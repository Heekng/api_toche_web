package com.heekng.api_toche_web.common;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommonTest {

    @Test
    void mapRemoveTest() throws Exception {
        //given
        List<String> testList = new ArrayList(List.of("one", "two", "three"));
        //when
        for (int i = 0; i < testList.size(); i++) {
            String testString = testList.get(i);
            if (testString.equals("two")) {
                testList.remove(i);
                i--;
            } else {
                System.out.println(testString);
            }
        }
    }
}

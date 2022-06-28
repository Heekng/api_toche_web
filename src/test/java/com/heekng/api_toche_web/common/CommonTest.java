package com.heekng.api_toche_web.common;

import com.heekng.api_toche_web.util.Fnv1aHash;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    @Test
    void cDragonHashTest() throws Exception {
        String text = "ShieldHealthPercent";
        text = text.toLowerCase();
        int hash = 0x811c9dc5;

        for (int i = 0; i < text.length(); i++) {
            hash ^= text.charAt(i);
            hash *= 0x1000193;
        }

        String x = Integer.toHexString(hash);
        int length = x.length();
        String result = "";
        for (int i = 0; i < 8-length; i++) {
            result += "0";
        }
        result += x;
        Assertions.assertThat(result).isEqualTo("0034a6ef");
    }

    @Test
    void cDragonHashTest2() throws Exception {
        String text = "ShieldHealthPercent";
        String result = Fnv1aHash.hashCDragonValue(text);
        Assertions.assertThat(result).isEqualTo("0034a6ef");
    }

    @Test
    void findStringByPatternTest() throws Exception {
        Pattern pattern = Pattern.compile("@(.*?)@");
        String desc = "추가 주문력 30.0 (조합 아이템 포함). 스킬로 마법 피해나 고정 피해를 입히면 대상을 불태워 @BurnDuration@초 동안 대상 최대 체력의 @BurnPercent@%만큼 고정 피해를 입히고 그동안 모든 체력 회복 효과 @GrievousWoundsPercent@% 감소<br><br><tftitemrules>[고유 - 중복 적용 불가]</tftitemrules>";
        Matcher matcher = pattern.matcher(desc);
        if (matcher.find()) {
            String findString = matcher.group(1);
            System.out.println(findString);
        }
    }

}

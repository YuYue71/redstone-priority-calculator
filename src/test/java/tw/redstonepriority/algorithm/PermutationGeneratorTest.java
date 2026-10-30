package tw.redstonepriority.algorithm;

import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class PermutationGeneratorTest {
    @Test void fiveElementPrefixCountAndUniqueness() {
        var rows=PermutationGenerator.generate(PermutationGenerator.parse("01234"));
        assertEquals(List.of("01234","10234","02134","20134","12034","21034","01324","10324","03124","30124","13024","31024"), rows.subList(0,12).stream().map(r->String.join("",r)).toList());
        assertEquals(120,rows.size()); assertEquals(120,new HashSet<>(rows).size());
    }
    @Test void smallerInputsAndParsing() {
        assertEquals(List.of("ABC","BAC","ACB","CAB","BCA","CBA"),PermutationGenerator.generate(PermutationGenerator.parse("ABC")).stream().map(r->String.join("",r)).toList());
        assertEquals(List.of("AB","BA"),PermutationGenerator.generate(PermutationGenerator.parse("AB")).stream().map(r->String.join("",r)).toList());
        assertEquals(List.of("😀","中"),PermutationGenerator.parse("😀中"));
        assertEquals(List.of("比較器","中繼器1"),PermutationGenerator.parse("比較器, 中繼器1"));
        assertThrows(IllegalArgumentException.class,()->PermutationGenerator.parse(""));
        assertThrows(IllegalArgumentException.class,()->PermutationGenerator.parse("AAB"));
    }
}

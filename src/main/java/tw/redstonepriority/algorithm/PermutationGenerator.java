package tw.redstonepriority.algorithm;

import java.math.BigInteger;
import java.util.*;

public final class PermutationGenerator {
    private static final int[][] THREE = {{0,1,2},{1,0,2},{0,2,1},{2,0,1},{1,2,0},{2,1,0}};
    private PermutationGenerator() {}

    public static List<String> parse(String input) {
        if (input == null || input.trim().isEmpty()) throw new IllegalArgumentException("請輸入至少一個元素。");
        List<String> tokens = new ArrayList<>();
        if (input.indexOf(',') >= 0) {
            for (String token : input.split(",", -1)) {
                String value = token.trim();
                if (value.isEmpty()) throw new IllegalArgumentException("逗號之間不可留空。");
                tokens.add(value);
            }
        } else input.codePoints().forEach(cp -> tokens.add(new String(Character.toChars(cp))));
        if (tokens.isEmpty()) throw new IllegalArgumentException("請輸入至少一個元素。");
        Set<String> unique = new HashSet<>();
        for (String token : tokens) if (!unique.add(token)) throw new IllegalArgumentException("元素不可重複：" + token);
        return List.copyOf(tokens);
    }

    public static BigInteger factorial(int n) {
        BigInteger result = BigInteger.ONE;
        for (int i=2; i<=n; i++) result = result.multiply(BigInteger.valueOf(i));
        return result;
    }

    public static List<List<String>> generate(List<String> input) {
        if (input == null || input.isEmpty()) throw new IllegalArgumentException("請輸入至少一個元素。");
        List<List<String>> output = new ArrayList<>();
        if (input.size() == 1) output.add(List.copyOf(input));
        else if (input.size() == 2) { output.add(List.copyOf(input)); output.add(List.of(input.get(1), input.get(0))); }
        else recurse(new ArrayList<>(input), new ArrayList<>(), output);
        return output;
    }

    private static void recurse(List<String> remaining, List<String> suffix, List<List<String>> out) {
        if (remaining.size() == 3) {
            for (int[] pattern : THREE) {
                List<String> row = new ArrayList<>(3 + suffix.size());
                for (int index : pattern) row.add(remaining.get(index));
                row.addAll(suffix); out.add(List.copyOf(row));
            }
            return;
        }
        for (int i=remaining.size()-1; i>=0; i--) {
            String chosen = remaining.get(i);
            List<String> next = new ArrayList<>(remaining); next.remove(i);
            List<String> nextSuffix = new ArrayList<>(suffix); nextSuffix.add(0, chosen);
            recurse(next, nextSuffix, out);
        }
    }
}

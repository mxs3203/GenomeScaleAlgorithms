package util;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CigarUtil {
    public static String compress(String inCig) {

        Map<Character, Character> map = new HashMap<>();

        map.put('=', 'M');
        map.put('X', 'M');
        map.put('D', 'D');
        map.put('I', 'I');

        int counter = 0;


        char[] cig = inCig.toCharArray();
        char current = 's';

        StringBuilder out = new StringBuilder();

        boolean started = false;

        for (char c : cig) {
            if (started) {
                if (map.get(c) != current) {
                    out.append((counter + 1) + Character.toString(current));
                    counter = 0;
                    current = map.get(c);
                } else {
                    counter++;
                }
            } else {
                started = true;
                current = map.get(c);
            }
        }
        out.append((counter + 1) + Character.toString(current));
        return out.toString();
    }

    public static String toProper(String str) {
        String result = "";

        int count = 0;
        char pre = '0';
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            switch (c) {
                case '=':
                case 'X':
                    if (pre == 'M') {
                        count++;
                    } else {
                        if (pre != '0')
                            result += count + String.valueOf(pre);
                        pre = 'M';
                        count = 1;
                    }
                    break;
                case 'D':
                    if (pre == 'D') {
                        count++;
                    } else {
                        if (pre != '0')
                            result += count + String.valueOf(pre);
                        pre = 'D';
                        count = 1;
                    }
                    break;
                case 'I':
                    if (pre == 'I') {
                        count++;
                    } else {
                        if (pre != '0')
                            result += count + String.valueOf(pre);
                        pre = 'I';
                        count = 1;
                    }
                    break;
            }
        }
        if (pre != '0')
            result += count + String.valueOf(pre);

        return result;
    }

    public static String mutate(String simpleCigar, String seq) {
        Random r = new Random();
        char[] cigarChars = simpleCigar.toCharArray();
        char[] seqChars = seq.toCharArray();
        char[] dnaChars = "ATGC".toCharArray();
        String result = "";
        int j = 0;
        for (int i = 0; i < cigarChars.length; i++) {
            char c = cigarChars[i];
            switch (c) {
                case '=':
                    result += String.valueOf(seqChars[j]);
                    j++;
                    break;
                case 'X':
                    result += String.valueOf(dnaChars[r.nextInt(4)]);
                    j++;
                    break;
                case 'D':
                    j++;
                    break;
                case 'I':
                    result += String.valueOf(dnaChars[r.nextInt(4)]);
                    break;
            }
        }
        return result;
    }
}

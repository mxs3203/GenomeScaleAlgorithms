package util;

import java.util.Random;

public class CigarUtil {
    public static String toProper(String str){
        String result = "";

        int count = 0;
        char pre = '0';
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++){
            char c = chars[i];
            switch (c){
                case '=':
                case 'X':
                    if(pre == 'M'){
                        count++;
                    } else {
                        if(pre!='0')
                            result += count + String.valueOf(pre);
                        pre = 'M';
                        count = 1;
                    }
                    break;
                case 'D':
                    if(pre == 'D'){
                        count++;
                    } else {
                        if(pre!='0')
                            result += count + String.valueOf(pre);
                        pre = 'D';
                        count = 1;
                    }
                    break;
                case 'I':
                    if(pre == 'I'){
                        count++;
                    } else {
                        if(pre!='0')
                            result += count + String.valueOf(pre);
                        pre = 'I';
                        count = 1;
                    }
                    break;
            }
        }
        if(pre!='0')
            result += count + String.valueOf(pre);

        return result;
    }

    public static String mutate(String simpleCigar, String seq){
        Random r = new Random();
        char[] cigarChars = simpleCigar.toCharArray();
        char[] seqChars = seq.toCharArray();
        char[] dnaChars = "ATGC".toCharArray();
        String result = "";
        int j = 0;
        for(int i = 0; i<cigarChars.length; i++){
            char c = cigarChars[i];
            switch (c){
                case '=':
                    result += String.valueOf(seqChars[j]);
                    j ++;
                    break;
                case 'X':
                    result += String.valueOf(dnaChars[r.nextInt(4)]);
                    j ++;
                    break;
                case 'D':
                    j ++;
                    break;
                case 'I':
                    result += String.valueOf(dnaChars[r.nextInt(4)]);
                    break;
            }
        }
        return result;
    }
}

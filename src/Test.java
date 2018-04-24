import algorithm.SuffixArray12;
import datastructur.SuffixArray;

import java.util.ArrayList;

public class Test {
    public static void main(String[] args) {
        int[] SA = SuffixArray.suffixArrayUsingSort("ATTCATG").getSuffixArray();
        for(int i: SA) System.out.println(i+"");

        SA = SuffixArray12.suffixArray("ATTCATG");
        for(int i: SA) System.out.println(i+"");
    }
}

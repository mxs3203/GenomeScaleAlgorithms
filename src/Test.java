import algorithm.SuffixArray12;
import datastructur.SuffixArray;
import util.FastaUtil;

import java.util.ArrayList;

public class Test {
    public static void main(String[] args) {

        String text = FastaUtil.getFirst("gorGor3-small-noN.fa");
        System.out.println("Ready!");
        long startTime = System.nanoTime();
        int[] SA_1 = SuffixArray12.suffixArray(text);
        long estimatedTime = System.nanoTime() - startTime;
        System.out.println("SA1 Done! - " + estimatedTime);
        startTime = System.nanoTime();
        int[] SA_2 = SuffixArray.suffixArrayUsingSort(text).getSuffixArray();
        estimatedTime = System.nanoTime() - startTime;
        System.out.println("SA2 Done! - " + estimatedTime);

        for(int i = 0; i<SA_1.length; i++){
            //System.out.println(SA_1[i] + ", " + SA_2[i]);
            if (SA_1[i] != SA_2[i]) {
                System.out.println("Error!");
                return;
            }
        }
        System.out.println("Success!");
    }
}

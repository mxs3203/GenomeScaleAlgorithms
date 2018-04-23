package datastructur;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class SuffixArray implements Serializable {
    private int[] suffixArray;
    private char[] text;
    private int n;

    private List<Character> alphabet;
    private HashMap<Character, Integer> alphabetMap;
    private int[] C;
    private int[][] O;

    private SuffixArray(int[] suffixArray, char[] text) {
        this.suffixArray = suffixArray;
        this.text = text;
        this.n = text.length;
        buildAlphabet();
        buildCTable();
        buildOTable();
    }

    public SuffixArray(int[] suffixArray, char[] text, int[][] oTable, int[] cTable) {
        this.suffixArray = suffixArray;
        this.text = text;
        this.O = oTable;
        this.C = cTable;
        this.n = text.length;
    }

    public List<Integer> binarySearch(char[] pattern){
        int j = -1, L = 0, R = n-1, M;
        do{
            M = (int) Math.ceil((R+L)/2);
            if (equals(pattern, suffixArray[M]) ){
                j = M;
            } else if (after(pattern, suffixArray[M])){
                L = M;
            } else {
                R = M;
            }
        } while (!(L == R || L == R-1 || j != -1));

        if(j == -1 )
            return Collections.EMPTY_LIST;

        List<Integer> result = report(
                findLeft(pattern, L, M),
                findRight(pattern, M, R));
        return result;
    }

    private int findLeft(char[] pattern, int L, int R) {
        int j = -1, M = 0;
        do{
            M = (int) Math.ceil((R+L)/2);
            if ((M == 0 || !equals(pattern, suffixArray[M-1])) && equals(pattern, suffixArray[M]) ){
                j = M;
            } else if (!equals(pattern, suffixArray[M])){
                L = M;
            } else {
                R = M;
            }
        } while (!(L == R || L == R-1 || j != -1));

        if(j==-1) return R;
        return j;
    }

    private int findRight(char[] pattern, int L, int R) {
        int j = -1, M;
        do{
            M = (int) Math.ceil((R+L)/2);
            if ((M == suffixArray.length-1 || !equals(pattern, suffixArray[M+1])) && equals(pattern, suffixArray[M]) ){
                j = M;
            } else if (equals(pattern, suffixArray[M])){
                L = M;
            } else {
                R = M;
            }
        } while (!(L == R || L == R-1 || j != -1));

        if(j==-1) {
            if (L != suffixArray.length-1 && equals(pattern, suffixArray[M+1])) {
                return L +1;
            } else {
                return L;
            }
        }
        return j;
    }

    public List<Integer> bw(char[] w){
        int m = w.length;
        int L = 0, R = n-1, i = m-1;
        while (i >= 0 && L <= R){
            Integer wi = alphabetMap.get(w[i]);
            if(wi == null)
                return Collections.EMPTY_LIST;

            L = C[wi] + (L == 0 ? 0: O[wi][L - 1]) + 1;
            R = C[wi] + O[wi][R];
            i--;
        }

        if (i < 0 && L <= R){
            return report(L, R);
        } else {
            return Collections.EMPTY_LIST;
        }
    }

    private List<Integer> report(int L, int R){
        List<Integer> result = new ArrayList<>();
        for (int i = L; i <= R; i++){
            result.add(suffixArray[i]);
        }
        return result;
    }

    private boolean after(char[] pattern, int start) {
        for (int i = 0; i < pattern.length; i++){
            if(i+start>text.length-1)
                return true;
            if (pattern[i] > text[i+start])
                return true;
            if (pattern[i] < text[i+start])
                return false;
        }
        return false;
    }

    private boolean equals(char[] pattern, int start) {
        if (start + pattern.length > text.length) return false;

        for (int i = 0; i < pattern.length; i++){
            if (pattern[i] != text[i+start])
                return false;
        }
        return true;
    }

    private void buildAlphabet(){
        alphabet = new ArrayList<>();
        alphabetMap = new HashMap<>();
        alphabet.add('$');
        for(int i = 1; i<suffixArray.length; i++){
            if(text[suffixArray[i]] != text[suffixArray[i-1]]){
                alphabet.add(text[suffixArray[i]]);
                alphabetMap.put(text[suffixArray[i]], alphabet.size()-1);
            }
        }

    }

    private void buildCTable(){
        C = new int[alphabet.size()];
        C[0] = 0;
        int index = 0;
        for(int i = 1; i<suffixArray.length; i++){
            if(text[suffixArray[i]] != text[suffixArray[i-1]]){
                index++;
                C[index] = i-1;
            }
        }
    }

    private void buildOTable(){
        O = new int[alphabet.size()][suffixArray.length];
        for(int i = 0; i < suffixArray.length; i++){
            for(int j = 0; j < alphabet.size(); j++){
                int SAi = suffixArray[i];
                SAi = SAi == 0? text.length: SAi;
                if (i == 0){
                    if(text[SAi-1] == alphabet.get(j))
                        O[j][i] = 1;
                    else
                        O[j][i] = 0;
                } else {
                    if(text[SAi-1]
                            == alphabet.get(j))
                        O[j][i] = O[j][i-1] + 1;
                    else
                        O[j][i] = O[j][i-1];
                }
            }
        }
    }

    public static SuffixArray suffixArrayUsingSort(String t){

        //init
        char[] text = (t+"$").toCharArray();
        List<Integer> list = new ArrayList<>();
        int n = text.length;
        for(int i = 0; i<text.length; i++){
            list.add(i);
        }

        //sort
        list.sort((i, j) -> {
            while (i < text.length && j < text.length && text[i] == text[j]) {
                i++;
                j++;
            }
            return text[i] - text[j];
        });


        //change format
        int[] suffixArray = new int[n];
        for(int i = 0; i<n;i++){
            suffixArray[i] = list.get(i);
        }
        return new SuffixArray(suffixArray, text);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for(int i: suffixArray){
            str.append(i).append(":\t");
            for(int j = i; j<text.length; j++){
                str.append(text[j]);
            }
            str.append("\n");
        }
        return str.toString();
    }

    public int[][] getOTable() {
        return O;
    }

    public int[] getCTable() {
        return C;
    }

    public int[] getSuffixArray() {
        return suffixArray;
    }

    public char[] getText() {
        return text;
    }
}

package datastructur;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SuffixTree<T> {
    Node<T> root;
    public char[] text;
    private String id;

    public SuffixTree(String t, String id) {
        text = (t + "$").toCharArray();
        this.id = id;
        root = new Node(-1, -1, -1);
        root.s = root;
        buildMcCreight();
    }

    public SuffixTree(char[] t) {
        text = new char[t.length + 1];
        for (int i = 0; i < t.length; i++) {
            text[i] = t[i];
        }
        text[t.length] = '$';
        root = new Node(-1, -1, -1);
        root.s = root;
        buildMcCreight();
    }

    public SuffixTree(char[] text, Node root) {
        this.text = text;
        this.root = root;
    }

    public List<Integer> matchesOfKDistance(char[] pattern, int k) {
        return matchesOfKDistance(pattern, k, root, 0, 0);
    }

    private List<Integer> matchesOfKDistance(char[] pattern, int k, Node<T> current, int labelIndex, int patternIndex) {
        List<Integer> result = new ArrayList<>();
        if (patternIndex >= pattern.length) {
            //report UBTREE
        } else if (labelIndex < (current.end - current.start)) {
            if (pattern[patternIndex] == text[current.start + labelIndex]) {
                matchesOfKDistance(pattern, k, current, labelIndex + 1, patternIndex + 1);
            } else if (k > 0) {
                matchesOfKDistance(pattern, k - 1, current, labelIndex + 1, patternIndex + 1);
            }
            if (k > 0) {
                matchesOfKDistance(pattern, k - 1, current, labelIndex, patternIndex + 1);
                matchesOfKDistance(pattern, k - 1, current, labelIndex + 1, patternIndex);
            }
        } else {
            for (Node child : current.children) {
                matchesOfKDistance(pattern, k, child, 0, patternIndex);
            }
        }
        return result;
    }

    private void buildMcCreight() {
        int n = text.length;
        Node[] head = new Node[n];
        head[0] = root;
        Node newNode = new Node(0, n - 1, 0);
        root.children.add(newNode);
        int tailI = 0;
        for (int i = 0; i < n - 1; i++) {
            int startIndex = i + 1;
            if (head[i] == root) {// if head(i) =  then
                Pair<Node, Integer> r = slowscanAndBuild(root, i + 1, n - 1);
                head[i + 1] = r.getKey();
                Integer tailINext = r.getValue();
                head[i + 1].children.add(new Node(tailINext, n - 1, startIndex, head[i + 1]));
                tailI = tailINext;
                continue;
            }
            Node u = head[i].parent;
            int vStart = head[i].start;
            int vEnd = head[i].end;//v = label(u,head(i))
            Node w = null;
            int wi = -1;
            if (u != root) {// u != ""
                Pair<Node, Integer> r = fastscanAndBuild(u.s, vStart, vEnd);//w = fastscan(s(u),v)//TODO brug fastscan
                w = r.getKey();
                wi = r.getValue();
            } else {
                Pair<Node, Integer> r = fastscanAndBuild(root, vStart + 1, vEnd);//w = fastscan(,v[2..|v|])//TODO brug fastscan
                w = r.getKey();
                wi = r.getValue();
            }

            int tailINext = -1;
            if (wi <= vEnd) {//if w is an edge then//TODO < eller <=
                head[i + 1] = w;
                tailINext = wi;
            } else if (wi > vEnd) {
                Pair<Node, Integer> r = slowscanAndBuild(w, tailI, n - 1);
                head[i + 1] = r.getKey();
                tailINext = r.getValue();
            }
            head[i].s = w;
            head[i + 1].children.add(new Node(tailINext, text.length - 1, startIndex, head[i + 1]));
            tailI = tailINext;
        }
    }

    private Pair<Node, Integer> fastscanAndBuild(Node<T> parent, int start, int end) {
        if (start <= end) {
            for (Node<T> child : parent.children) {
                if (text[child.start] == text[start]) {
                    int k = Math.min(end - start + 1, child.end - child.start + 1);

                    if (end - start + 1 < child.end - child.start + 1) {//missmatch found in edge label
                        Node newNode = new Node(child.start, child.start + k - 1, -1);
                        child.start = child.start + k;
                        parent.children.remove(child);
                        newNode.children.add(child);
                        child.parent = newNode;
                        parent.children.add(newNode);
                        newNode.parent = parent;
                        return fastscanAndBuild(newNode, start + k, end);
                    } else {//edge label completly matched
                        return fastscanAndBuild(child, start + k, end);
                    }
                }
            }
        }
        return new Pair<>(parent, start);
    }


    private Pair<Node, Integer> slowscanAndBuild(Node<T> parent, int start, int end) {//TODO USE end!!!
        if (start <= end) {
            for (Node<T> child : parent.children) {
                if (text[child.start] == text[start]) {
                    int k = 1;
                    while (child.start + k <= child.end && start + k <= end && text[child.start + k] == text[start + k]) {//look though edge label
                        k++;
                    }

                    if ((text[child.start + k]
                            != text[start + k]
                            || start + k > end) && child.start + k <= child.end) {//missmatch found in edge label
                        Node newNode = new Node(child.start, child.start + k - 1, -1);
                        child.start = child.start + k;
                        parent.children.remove(child);
                        newNode.children.add(child);
                        child.parent = newNode;
                        parent.children.add(newNode);
                        newNode.parent = parent;
                        return slowscanAndBuild(newNode, start + k, end);
                    } else {//edge label completly matched
                        return slowscanAndBuild(child, start + k, end);
                    }
                }
            }
        }
        return new Pair<>(parent, start);
    }

    private boolean headIsEmpty(Node<T> head, int i) {
        for (Node<T> child : head.children) {
            if (text[child.start] == text[i]) {
                return false;
            }
        }
        return true;
    }

    private void build() {
        int n = text.length;
        for (int i = 0; i < n; i++) {
            build(root, i, i);
        }
    }

    private void build(Node parent, int i, int startIndex) {

        boolean matched = false;
        ArrayList<Node> children = new ArrayList<>(parent.children);
        for (Node child : children) {
            if (text[child.start] == text[i]) {
                matched = true;
                int k = 1;
                while (text[child.start + k] == text[i + k] && child.start + k < child.end) {//look though edge label
                    k++;
                }

                if (text[child.start + k] != text[i + k] && child.start + k <= child.end) {//missmatch found in edge label
                    Node newNode = new Node(child.start, child.start + k - 1, -1);
                    child.start = child.start + k;
                    parent.children.remove(child);
                    newNode.children.add(child);
                    parent.children.add(newNode);
                    build(newNode, i + k, startIndex);
                } else {//edge label completly matched
                    build(child, i + k, startIndex);
                }
            }
        }

        if (!matched) {//no matching child
            parent.children.add(new Node(i, text.length - 1, startIndex));
        }
    }

    public List<Integer> match(char[] pattern) {
        return match(root, pattern, 0);
    }

    public static List<Integer> match(char[] text, char[] pattern) {
        return new SuffixTree(text).match(pattern);
    }

    public List<Integer> farstMatch(char[] pattern) {
        return farstMatch(root, pattern, 0);
    }

    /**
     * Using a suffix tree for exact pattern matching
     * Say that we want to find the occurrences of a pattern (of length m) in text (of length n), and that we know that
     * the pattern occurs at least once. Using a suffix tree we can solve this problem in worst case time O(m + z),
     * where z is the number of occurrences, but can it be done faster in the best case since we know that the pattern
     * occurs at least once in the text?
     *
     * @param parent
     * @param pattern
     * @param i
     * @return
     * @postcondition pattern must exist in the tree
     */
    private List<Integer> farstMatch(Node<T> parent, char[] pattern, int i) {
        ArrayList<Integer> result = new ArrayList<>();
        for (Node<T> child : parent.children) {
            int k = 0;
            if (text[child.start] == pattern[i]) {
                k = child.end - child.start + 1;
            }
            if (i + k >= pattern.length) {
                result.addAll(reportSubTree(child));
            } else if (child.start + k > child.end) {
                result.addAll(farstMatch(child, pattern, i + k));
            }
        }
        return result;
    }

    /*
    Using a suffix tree for counting the number of non-overlapping occurrences of pattern
    Using a suffix tree for a text (of length n), we can easily count the number of occurrences of a pattern (of length
    m) in the text in time O(m). How fast can you count the maximal number of non-overlapping occurrences of a pattern
    (of length m) in the text?

    - Get the result, greedy select the non over lapping results, looking at the start index and the length of the
    pattern. This takes O(m+z).

    Suffix links
    Recall the definition of a suffix link from the lecture. Show that if u is a node in a suffix tree, then the suffix
    link, s(u), is also a node.

    - The suffix link points to s(u)=v | u = av, but in a suffix tree we will also have all suffixes of all nodes, the
    nodes exists if there is an overlap and if we have an overlap we will also have an("the same") overlap of the
    suffixes that are one shorter.
     */

    private List<Integer> match(Node<T> parent, char[] pattern, int i) {
        ArrayList<Integer> result = new ArrayList<>();
        for (Node<T> child : parent.children) {
            int k = 0;
            while (i + k < pattern.length && text[child.start + k] == pattern[i + k] && child.start + k <= child.end) {
                k++;
            }

            if (i + k == pattern.length) {
                result.addAll(reportSubTree(child));
            } else if (child.start + k > child.end) {
                result.addAll(match(child, pattern, i + k));
            }
        }
        return result;
    }

    private List<Integer> reportSubTree(Node<T> parent) {
        List<Integer> result = new ArrayList<>();
        if (parent.children.size() == 0) {
            result.add(parent.startIndex);
        }

        for (Node<T> child : parent.children) {
            result.addAll(reportSubTree(child));
        }

        return result;
    }

    /*public SuffixArray toSuffixArray(){
        int[] suffixArray = new int[text.length];
        buildSuffixArray(root, suffixArray, 0);
        return new SuffixArray(suffixArray, text);
    }*/

    private int buildSuffixArray(Node<T> current, int[] suffixArray, int i) {
        Collections.sort(current.children, (o1, o2) -> {
            if (text[o1.start] == '$') return -1;
            if (text[o2.start] == '$') return 1;
            return text[o1.start] - text[o2.start];
        });
        for (Node<T> child : current.children) {
            i = buildSuffixArray(child, suffixArray, i);
        }
        if (current.children.size() == 0) {
            suffixArray[i] = current.startIndex;
            i++;
        }
        return i;
    }

    public static int countNonOverlappingOccurrences(char[] pattern) {
        return 0;
    }

    public Node<T> getRoot() {
        return root;
    }

    public String getId() {
        return id;
    }

    public static class Node<T> {
        public Node<T> parent;
        Node<T> s;
        public int start;
        public int end;
        public int startIndex;
        public ArrayList<Node<T>> children = new ArrayList<>();
        T extra;

        public Node(int start, int end, int startIndex) {
            this.start = start;
            this.end = end;
            this.startIndex = startIndex;
        }

        public void setExtra(T extra) {
            this.extra = extra;
        }

        public T getExtra() {
            return extra;
        }

        public Node(Integer start, int end, int startIndex, Node parent) {
            this(start, end, startIndex);
            this.parent = parent;
        }
    }
}

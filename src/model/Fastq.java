package model;

public class Fastq {
    private final String seq;
    private final String quality;

    public Fastq(String seq, String quality) {
        this.seq = seq;
        this.quality = quality;
    }

    public String getSeq() {
        return seq;
    }

    public String getQuality() {
        return quality;
    }

    @Override
    public String toString() {
        return "{"+seq+", "+quality+"}";
    }
}

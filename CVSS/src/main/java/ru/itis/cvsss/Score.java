package ru.itis.cvsss;


public class Score {

    private double baseScore;
    private double impactSubScore;
    private double exploitabilitySubScore;
    private double temporalScore;

    public Score(double baseScore, double impactSubScore, double exploitabilitySubScore) {
        this(baseScore, impactSubScore, exploitabilitySubScore, -1);
    }

    public Score(double baseScore, double impactSubScore, double exploitabilitySubScore, double temporalScore) {
        this.baseScore = baseScore;
        this.impactSubScore = impactSubScore;
        this.exploitabilitySubScore = exploitabilitySubScore;
        this.temporalScore = temporalScore;
    }


    public double getBaseScore() {
        return baseScore;
    }

    public double getImpactSubScore() {
        return impactSubScore;
    }

    public double getExploitabilitySubScore() {
        return exploitabilitySubScore;
    }

    public double getTemporalScore() {
        return temporalScore;
    }
}

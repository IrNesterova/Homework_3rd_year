package ru.itis.cvsss;

import us.springett.cvss.Cvss;

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface CVSS {
    String V2_PATTERN = "AV:[NAL]\\/AC:[LMH]\\/A[Uu]:[NSM]\\/C:[NPC]\\/I:[NPC]\\/A:[NPC]";
    String V2_TEMPORAL = "\\/E:\\b(F|H|U|POC|ND)\\b\\/RL:\\b(W|U|TF|OF|ND)\\b\\/RC:\\b(C|UR|UC|ND)\\b";

    String V3_PATTERN = "AV:[NALP]\\/AC:[LH]\\/PR:[NLH]\\/UI:[NR]\\/S:[UC]\\/C:[NLH]\\/I:[NLH]\\/A:[NLH]";
    String V3_TEMPORAL = "\\/E:[F|H|U|P|X]\\/RL:[W|U|T|O|X]\\/RC:[C|R|U|X]";

    Pattern CVSSv2_PATTERN = Pattern.compile(V2_PATTERN);
    Pattern CVSSv2_PATTERN_TEMPORAL = Pattern.compile(V2_PATTERN + V2_TEMPORAL);
    Pattern CVSSv3_PATTERN = Pattern.compile(V3_PATTERN);
    Pattern CVSSv3_PATTERN_TEMPORAL = Pattern.compile(V3_PATTERN + V3_TEMPORAL);

    Score calculateScore();
    String getVector();

    static Cvss fromVector(String vector) {
        if (vector == null) {
            return null;
        }
        Matcher v2Matcher = CVSSv2_PATTERN.matcher(vector);
        Matcher v2TemporalMatcher = CVSSv2_PATTERN_TEMPORAL.matcher(vector);
        Matcher v3Matcher = CVSSv3_PATTERN.matcher(vector);
        Matcher v3TemporalMatcher = CVSSv3_PATTERN_TEMPORAL.matcher(vector);

        if (v2TemporalMatcher.find()) {
            // Found a valid CVSSv2 vector with temporal values
            String matchedVector = v2TemporalMatcher.group(0);
            StringTokenizer st = new StringTokenizer(matchedVector, "/");
            CvssV2 cvssV2 = getCvssV2BaseVector(st);
            cvssV2.exploitability(CvssV2.Exploitability.fromString(st.nextElement().toString().split(":")[1]));
            cvssV2.remediationLevel(CvssV2.RemediationLevel.fromString(st.nextElement().toString().split(":")[1]));
            cvssV2.reportConfidence(CvssV2.ReportConfidence.fromString(st.nextElement().toString().split(":")[1]));
            return cvssV2;
        } else if (v2Matcher.find()) {
            // Found a valid CVSSv2 vector
            String matchedVector = v2Matcher.group(0);
            StringTokenizer st = new StringTokenizer(matchedVector, "/");
            return getCvssV2BaseVector(st);
        } else if (v3TemporalMatcher.find()) {
            // Found a valid CVSSv3 vector with temporal values
            String matchedVector = v3TemporalMatcher.group(0);
            StringTokenizer st = new StringTokenizer(matchedVector, "/");
            CvssV3 cvssV3 = getCvssV3BaseVector(st);
            cvssV3.exploitability(CvssV3.Exploitability.fromString(st.nextElement().toString().split(":")[1]));
            cvssV3.remediationLevel(CvssV3.RemediationLevel.fromString(st.nextElement().toString().split(":")[1]));
            cvssV3.reportConfidence(CvssV3.ReportConfidence.fromString(st.nextElement().toString().split(":")[1]));
            return cvssV3;
        } else if (v3Matcher.find()) {
            // Found a valid CVSSv3 vector
            String matchedVector = v3Matcher.group(0);
            StringTokenizer st = new StringTokenizer(matchedVector, "/");
            return getCvssV3BaseVector(st);
        }
        return null;
    }

    static CvssV2 getCvssV2BaseVector(StringTokenizer st) {
        CvssV2 cvssV2 = new CvssV2();
        cvssV2.attackVector(CvssV2.AttackVector.fromString(st.nextElement().toString().split(":")[1]));
        cvssV2.attackComplexity(CvssV2.AttackComplexity.fromString(st.nextElement().toString().split(":")[1]));
        cvssV2.authentication(CvssV2.Authentication.fromString(st.nextElement().toString().split(":")[1]));
        cvssV2.confidentiality(CvssV2.CIA.fromString(st.nextElement().toString().split(":")[1]));
        cvssV2.integrity(CvssV2.CIA.fromString(st.nextElement().toString().split(":")[1]));
        cvssV2.availability(CvssV2.CIA.fromString(st.nextElement().toString().split(":")[1]));
        return cvssV2;
    }

    static CvssV3 getCvssV3BaseVector(StringTokenizer st) {
        CvssV3 cvssV3 = new CvssV3();
        cvssV3.attackVector(CvssV3.AttackVector.fromString(st.nextElement().toString().split(":")[1]));
        cvssV3.attackComplexity(CvssV3.AttackComplexity.fromString(st.nextElement().toString().split(":")[1]));
        cvssV3.privilegesRequired(CvssV3.PrivilegesRequired.fromString(st.nextElement().toString().split(":")[1]));
        cvssV3.userInteraction(CvssV3.UserInteraction.fromString(st.nextElement().toString().split(":")[1]));
        cvssV3.scope(CvssV3.Scope.fromString(st.nextElement().toString().split(":")[1]));
        cvssV3.confidentiality(CvssV3.CIA.fromString(st.nextElement().toString().split(":")[1]));
        cvssV3.integrity(CvssV3.CIA.fromString(st.nextElement().toString().split(":")[1]));
        cvssV3.availability(CvssV3.CIA.fromString(st.nextElement().toString().split(":")[1]));
        return cvssV3;
    }
}

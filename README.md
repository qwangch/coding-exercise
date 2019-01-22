# Validity Take Home excercise

This coding test uses java-string-similarity library (https://github.com/tdebatty/java-string-similarity) to compute the fuzzy match.This library implementing A dozen of algorithms (including Levenshtein edit distance and sibblings, Jaro-Winkler, Longest Common Subsequence,cosine similarity etc.). After few tests, I selected the one that I think it's best for the fuzzy macth.

## Setup

1. Install maven on mac

        brew install maven

2. Start the app locally

        mvn clean spring-boot:run

    Build it into a jar and run it from a jar

        mvn clean package

        java -jar target/take-home-test-1.0.jar

## Test it

### curl

The TomCat server will run on port 8080

    curl -v "http://localhost:8080/"

### lunch from the web

    http://localhost:8080/

## Troubleshooting

If the TomCat server cannot run because something else is running on the same port:

    ✗ lsof -iTCP:8080
    COMMAND   PID    USER   FD
    java    65349 bhatiar  165u
    ✗ kill -9 65349

## TO DO
 unit tests

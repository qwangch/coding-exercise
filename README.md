# Validity Take Home excercise

## Setup

1. Install maven on mac

        brew install maven

2. Start the app locally

        mvn clean spring-boot:run

    Build it into a jar and run it from a jar

        mvn clean package

        java -jar target/take-home-test-1.0.jar

## Test it

### Unit tests

Unit tests will run every time you run and build the app. To run only the unit tests, run the following.

    mvn test

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
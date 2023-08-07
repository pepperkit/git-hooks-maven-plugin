FROM alpine:latest
RUN apk update
RUN apk add git
RUN apk add maven

# Install plugin from sources
COPY pom.xml plugin/pom.xml
COPY src/main plugin/src/main
COPY README.md target* plugin/target/
RUN cd plugin && mvn clean install -DskipTests=true -Dgpg.skip

# Copy test project
COPY src/system-test/resources/* test-projects/

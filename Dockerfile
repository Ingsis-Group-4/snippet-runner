FROM gradle:8.5-jdk21 AS build

ARG ACTOR
ARG TOKEN

ENV GITHUB_ACTOR ${ACTOR}
ENV GITHUB_TOKEN ${TOKEN}


COPY  . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle assemble

FROM amazoncorretto:21-alpine
EXPOSE 8080
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/spring-boot-application.jar
COPY --from=build /home/gradle/src/newrelic/newrelic.jar /newrelic.jar
COPY --from=build /home/gradle/src/newrelic/newrelic.yml /newrelic.yml
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=production","-javaagent:/newrelic.jar","/app/spring-boot-application.jar"]
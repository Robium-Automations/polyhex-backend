#  docker build -t polyhex_backend .

FROM openjdk:8 AS TEMP_BUILD_IMAGE
ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME
COPY build.gradle settings.gradle gradlew $APP_HOME
COPY gradle $APP_HOME/gradle
RUN ./gradlew build || return 0
COPY . .
RUN ./gradlew build

FROM openjdk:8
ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME
COPY --from=TEMP_BUILD_IMAGE $APP_HOME/build/libs/polyhex-0.0.1-SNAPSHOT.war .
EXPOSE 8080
CMD ["java","-jar", "polyhex-0.0.1-SNAPSHOT.war"]
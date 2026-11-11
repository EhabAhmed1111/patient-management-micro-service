# define the builder phase

# we will build from eclipse image

FROM gradle:8.14-jdk21 AS builder

WORKDIR /app
# we need to copy gradle to be download
COPY gradlew .
COPY gradle ./gradle
COPY build.gradle.kts settings.gradle.kts ./

# this will make gradlew excutable so you can use ./gradlew
RUN chmod +x gradlew
RUN ./gradlew dependencies --no-daemon
# next we need to send the src
COPY src ./src

# then we will run ./gradlew build
RUN ./gradlew clean bootJar -x test --no-daemon

# now we need runner phase

# first the runner image
FROM eclipse-temurin:21-jre AS runner


WORKDIR /app

# copy the genrated jar into the files
COPY --from=builder ./app/build/libs/analytics-service.jar ./app.jar

# expose port
EXPOSE 4002

# use cmd or Run
ENTRYPOINT ["java", "-jar", "app.jar"]



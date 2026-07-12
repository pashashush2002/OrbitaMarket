# Build stage
FROM gradle:9.5-jdk25 AS build
WORKDIR /app
COPY . .
RUN gradle build -x test --no-daemon
# Run stage
FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
RUN addgroup --system spring && adduser --system spring --ingroup spring
USER spring
EXPOSE 8081
CMD ["java", "-jar", "app.jar"]

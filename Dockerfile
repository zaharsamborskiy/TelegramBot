FROM eclipse-temurin:17-jdk-jammy
ADD /out/artifacts/TelegramBot_jar/TelegramBot.jar backend.jar
ENTRYPOINT ["java", "-jar", "backend.jar"]

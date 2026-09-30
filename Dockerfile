FROM maven:3.9.11-eclipse-temurin-17

WORKDIR /workspace

COPY pom.xml .

RUN mvn -B dependency:go-offline

COPY src ./src
COPY testng.xml .

CMD ["mvn", "-B", "test", "-Dexecution=remote", "-Dselenium.url=http://selenium-chrome:4444/wd/hub"]
FROM node:18 AS jsbuild
WORKDIR /app
COPY ./pefi-frontend .
RUN npm ci && \
    npm run build

FROM ghcr.io/graalvm/graalvm-ce:ol8-java17-22.2.0
WORKDIR /app

COPY ./pefi-backend/.mvn/ .mvn
COPY ./pefi-backend/mvnw ./pefi-backend/pom.xml ./

COPY ./pefi-backend/src ./src
COPY --from=jsbuild /app/build/ ./src/main/resources/static/

RUN ./mvnw package -DskipTests
    
EXPOSE 8080
CMD ["java", "-jar", "target/pefi-backend-0.0.1-SNAPSHOT.jar"]
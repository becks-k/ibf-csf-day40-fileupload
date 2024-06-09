# Build angular
FROM node:22 AS ngbuild

# directory INSIDE image
WORKDIR /frontend

# Install angular
RUN npm i -g @angular/cli@17.3.8

# Copy the required files for building
# from directory FRONTEND on local machine
COPY frontend/angular.json .
COPY frontend/package*.json .
COPY frontend/tsconfig*.json .
COPY frontend/src src

# Install modules - ci installs packages from package-lock.json
# npm ci stands for "npm clean install." It removes the node_modules folder and installs dependencies from scratch based exactly on the package-lock.json file.
RUN npm ci
RUN ng build

# Build Spring boot
FROM openjdk:21 AS javabuild

WORKDIR /backend

# . refers to destination in docker container /backend
# copies mvn files in backend/.mvn into /backend/.mvn
COPY backend/mvnw .
COPY backend/pom.xml .
COPY backend/.mvn .mvn
COPY backend/src src


# Copy angular files to Spring Boot
COPY --from=ngbuild /frontend/dist/frontend/browser/ src/main/resources/static

# Compile SpringBoot
# produce target/backend-0.0.1-SNAPSHOT.jar
# don't run tests!
# ./mvnw is a maven wrapper script that ensures the correct maven is used, regardless of version installed on host machine
RUN ./mvnw package -Dmaven.test.skip=true

# Run container
FROM openjdk:21

WORKDIR /app

COPY --from=javabuild /backend/target/day40-0.0.1-SNAPSHOT.jar app.jar

ENV PORT=8080 SPACES_KEY_SECRET= SPACES_KEY_ACCESS= 

EXPOSE ${PORT}

# starts container
ENTRYPOINT SERVER_PORT=${PORT} java -jar app.jar .
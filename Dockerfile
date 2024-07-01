FROM --platform=linux/amd64 gradle:jdk21 AS builder

WORKDIR /build

COPY build.gradle.kts settings.gradle.kts /build/
RUN gradle build -x test --parallel --continue > /dev/null 2>&1 || true

COPY . /build
RUN gradle build -x test --parallel

FROM --platform=linux/amd64 openjdk:21-jdk-slim
WORKDIR /app

COPY --from=builder /build/build/libs/gem-api-0.0.1-SNAPSHOT.jar gem-app.jar

EXPOSE 8080

# root 대신 nobody 권한으로 실행
USER nobody
ENTRYPOINT [                                                \
    "java",                                                 \
    "-jar",                                                 \
    "-Djava.security.egd=file:/dev/./urandom",              \
    "-Dsun.net.inetaddr.ttl=0",                             \
    "gem-app.jar"                                           \
]

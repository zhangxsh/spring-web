FROM registry.cn-beijing.aliyuncs.com/alexzhang/openjdk:9.0
RUN mkdir -p /var/web
ARG JAR_FILE
COPY ${JAR_FILE} /var/web/spring-web.jar
ENTRYPOINT ["java","-Dserver.port=8080","-DLOG_PATH=/var/web","-jar", "/var/web/spring-web.jar"]
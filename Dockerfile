FROM registry.cn-beijing.aliyuncs.com/alexzhang/openjdk:9.0
RUN mkdir -p /var/web
COPY ./target/spring-web.jar /var/web/spring-web.jar
ENTRYPOINT ["java","-Dserver.port=8080","-DLOG_PATH=/var/web","-jar", "/var/web/spring-web.jar"]
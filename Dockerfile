FROM registry.cn-beijing.aliyuncs.com/xianshuangzhang/openjdk:9.0
RUN mkdir -p /var/web
COPY ./target/spring-web.jar /var/web/spring-web.jar
ENTRYPOINT ["java","-jar","/var/web/spring-web.jar","--server.port=8080"]
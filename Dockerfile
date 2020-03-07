FROM registry.cn-beijing.aliyuncs.com/xianshuangzhang/openjdk:9.0
COPY ./target/spring-web.jar ~/spring-web.jar
ENTRYPOINT ["java","-jar","~/spring-web.jar","--server.port=8080"]
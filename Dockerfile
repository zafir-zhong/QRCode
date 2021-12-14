# docker build -t tool/QRCode:1.0.00-release .

FROM openjdk:17-alpine

RUN \
    ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo "Asia/Shanghai" > /etc/timezone && \
    mkdir -p /app && \
    mkdir -p /app/images
# 路径请自行核对
ADD  /target/QRCode.jar /app/QRCode.jar

ENV JAVA_OPTS="-Duser.timezone=Asia/Shanghai"
EXPOSE 8080
ENV APP_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS  -jar /app/QRCode.jar $APP_OPTS" ]
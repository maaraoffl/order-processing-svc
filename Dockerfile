FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD target/order-intake-svc-0.0.1-SNAPSHOT.jar order-intake-svc.jar
RUN sh -c 'touch /order-intake-svc.jar'
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /order-intake-svc.jar" ]

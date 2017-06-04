FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD target/order-processing-svc-0.0.1-SNAPSHOT.jar order-processing-svc.jar
RUN sh -c 'touch /order-processing-svc.jar'
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /order-processing-svc.jar" ]

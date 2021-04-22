FROM tdengine/tdengine as builder
FROM ubuntu:latest

RUN apt-get clean && apt-get update && apt-get install --no-install-recommends -y openjdk-11-jdk
COPY engine.jar /engine.jar

COPY --from=builder /usr/local/taos/driver/libtaos.so.* /usr/local/taos/driver/libtaos.so.*

RUN ln -s /usr/local/taos/driver/libtaos.so.* /usr/lib/libtaos.so.1
RUN ln -s /usr/lib/libtaos.so.1 /usr/lib/libtaos.so

ENTRYPOINT ["java","-jar","/engine.jar"]
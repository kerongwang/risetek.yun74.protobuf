FROM ubuntu:latest

RUN apt-get clean && apt-get update && apt-get install --no-install-recommends -y locales openjdk-11-jdk

COPY engine.jar /engine.jar
ADD http://firmware.risetek.com/TDengine-server-2.0.19.1-Linux-x64.tar.gz /TDengine-server-2.0.19.1-Linux-x64.tar.gz
## ADD http://firmware.risetek.com/jdk-11_linux-x64_bin.tar.gz /jdk-11_linux-x64_bin.tar.gz
RUN tar -zxf /TDengine-server-2.0.19.1-Linux-x64.tar.gz
RUN cd /TDengine-server-2.0.19.1 && ./install.sh -e no -i service

COPY docker-entrypoint.sh /entrypoint.sh


RUN locale-gen en_US.UTF-8
ENV LD_LIBRARY_PATH="$LD_LIBRARY_PATH:/usr/lib"
ENV LC_CTYPE=en_US.UTF-8
ENV LANG=en_US.UTF-8
ENV LC_ALL=en_US.UTF-8

EXPOSE 6030 6031 6032 6033 6034 6035 6036 6037 6038 6039 6040 6041 6042
ENTRYPOINT ["/entrypoint.sh"]
CMD ["taosd"]
VOLUME [ "/var/lib/taos", "/var/log/taos","/etc/taos/" ]

## CMD ["/bin/bash"]
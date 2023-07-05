FROM jenkins/agent
USER root
RUN apt-get update -y
RUN apt-get install -y unzip
WORKDIR /gradle
RUN curl -L https://services.gradle.org/distributions/gradle-7.0.1-bin.zip -o gradle-7.0.1-bin.zip \
    && unzip gradle-7.0.1-bin.zip
ENV GRADLE_HOME=/gradle/gradle-7.0.1
ENV PATH=$PATH:$GRADLE_HOME/bin
RUN gradle --version
	

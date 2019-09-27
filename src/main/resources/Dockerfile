FROM java:8
VOLUME /tmp
ADD /d/workspace/Activiti6_com/modules/businessPro/target/businessPro.jar businessPro.jar
RUN bash -c 'touch /businessPro.jar'
ENTRYPOINT ["java","-jar","/businessPro.jar"]
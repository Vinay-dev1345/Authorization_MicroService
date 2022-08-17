FROM public.ecr.aws/amazoncorretto/amazoncorretto:17
EXPOSE 9003
ADD target/jwtAuthorization-0.0.1-SNAPSHOT.jar jwtAuthorization-0.0.1-SNAPSHOT.jar 
ENTRYPOINT ["java","-jar","/jwtAuthorization-0.0.1-SNAPSHOT.jar"]
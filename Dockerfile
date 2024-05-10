FROM openjdk:17-alpine
ENTRYPOINT ["/usr/bin/parcel-service.sh"]

COPY parcel-service.sh /usr/bin/parcel-service.sh
COPY target/parcel-service.jar /usr/share/parcel-service/parcel-service.jar

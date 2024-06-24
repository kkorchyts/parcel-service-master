# Parcel Service Documentation

Welcome to the Parcel Service documentation. This guide provides an overview of the gRPC service, setup instructions, and detailed information on how to interact with the service.

## Table of Contents

1. [Overview](#overview)
2. [Prerequisites](#prerequisites)
3. [Setup](#setup)
4. [Running Service](#running-service)
5. [API Reference and examples](#api-reference-and-examples)
7. [Troubleshooting](#troubleshooting)
8. [Additional Resources](#additional-resources)

## Overview

The Parcel Service is a gRPC-based service implemented with Spring Boot. It allows for the creation, management, and tracking of parcels. The service uses protocol buffers for defining the service and message structures.

## Prerequisites

Ensure you have the following installed before setting up the service:

- Java Development Kit (JDK) 11 or higher
- Apache Maven (for building the project)
- An IDE (e.g., IntelliJ IDEA, Eclipse)
- `grpcurl` (for interacting with the service)

## Setup

To set up the Parcel Service locally, follow these steps:

1. **Clone the Repository:**
   ```bash
   git clone https://github.com/svetamint/kkorchyts-parcel-service
   cd parcel-service
   mvn clean install

## Running Service

For running parcel-service, please, follow the next command:
  ```bash
  mvn spring-boot:run
  ```
The service will start on port 8000 by default.

## API Reference and examples

**Service Methods**
`CreateParcel` -  Creates a new parcel.

  **Request: CreateParcelDto**
  ```
  double weight = 1 [(validate.rules).double.gt = 0.0];
  double length = 2 [(validate.rules).double.gt = 0.0];
  double width = 3 [(validate.rules).double.gt = 0.0];
  double height = 4 [(validate.rules).double.gt = 0.0];
  string destination = 5;
  uint64 user_id = 7 [(validate.rules).uint64.gte = 1];
 ```

Example:
```
grpcurl -plaintext -d '{
  "weight": 2.5,
  "length": 10.0,
  "width": 5.0,
  "height": 3.0,
  "destination": "123 Main St, Anytown, USA",
  "user_id": 1
}' localhost:8080 parcel.ParcelService/CreateParcel
```

**Response: ParcelDto**
```
{
  "tracking_number": "TRACK12345678",
  "weight": 2.5,
  "length": 10.0,
  "width": 5.0,
  "height": 3.0,
  "destination": "123 Main St, Anytown, USA",
  "user_id": 1
}
```

`FindAllParcel` - Retrieves all parcels.

**Request: google.protobuf.Empty**

Example:
```
grpcurl -plaintext localhost:8080 parcel.ParcelService/FindAllParcel
```

**Response: ParcelDto**
```
{
  "count": 2,
  "parcel_dtos": [
    {
      "tracking_number": "TRACK12345678",
      "weight": 2.5,
      "length": 10.0,
      "width": 5.0,
      "height": 3.0,
      "destination": "123 Main St, Anytown, USA",
      "user_id": 1
    },
    {
      "tracking_number": "TRACK87654321",
      "weight": 1.5,
      "length": 8.0,
      "width": 4.0,
      "height": 2.0,
      "destination": "456 Elm St, Othertown, USA",
      "user_id": 2
    }
  ]
}
```

`FindParcelByTrackingNumber` - Finds a parcel by its tracking number.
**Request: TrackingNumberDto**

```
{
  string tracking_number = 1 [(validate.rules).string.pattern = "TRACK[1-9]{8}$"];
}
```

Example:
```
grpcurl -plaintext -d '{
  "tracking_number": "TRACK12345678"
}' localhost:8080 parcel.ParcelService/FindParcelByTrackingNumber
```

**Response - ParcelDto**

```
{
  "tracking_number": "TRACK12345678",
  "weight": 2.5,
  "length": 10.0,
  "width": 5.0,
  "height": 3.0,
  "destination": "123 Main St, Anytown, USA",
  "user_id": 1
}
```

`DeleteParcelByTrackingNumber` - Deletes a parcel by its tracking number.

**Request: TrackingNumberDto** the structure of this dto you can find above.

Example:
```
grpcurl -plaintext -d '{
  "tracking_number": "TRACK12345678"
}' localhost:8080 parcel.ParcelService/DeleteParcelByTrackingNumber
```

**Response: google.protobuf.Empty**
```
{}
```

`CancelParcelByTrackingNumber` - Cancels a parcel by its tracking number.
**Request: TrackingNumberDto** the structure of this dto you can find above.

Example:
```
grpcurl -plaintext -d '{
  "tracking_number": "TRACK12345678"
}' localhost:8080 parcel.ParcelService/DeleteParcelByTrackingNumber
```

**Response: google.protobuf.Empty**
```
{}
```

`UpdateParcel` - Updates the details of a parcel.
**Request: ParcelDto**

Example:
```
grpcurl -plaintext -d '{
  "destination": "456 Elm St, Othertown, USA",
  "user_id": 3
}' localhost:8080 parcel.ParcelService/UpdateParcel
```
**Response: ParcelDto**
```
grpcurl -plaintext -d '{
  "tracking_number": "TRACK12345609",
  "weight": 3.0,
  "length": 12.0,
  "width": 6.0,
  "height": 4.0,
  "destination": "456 Elm St, Othertown, USA",
  "user_id": 3
}' localhost:8080 parcel.ParcelService/UpdateParcel

```

`UpdateTrackingStatus` - Updates the tracking status of a parcel.
**Request: UpdateTrackingStatusDto**
```
{
  string tracking_number = 1 [(validate.rules).string.pattern = "TRACK[1-9]{8}$"];
  TrackingStatus new_status = 2 [(validate.rules).enum.defined_only = true];
}
```

Example:
```
grpcurl -plaintext -d '{
  "tracking_number": "TRACK12345678",
  "new_status": "DELIVERED"
}' localhost:8080 parcel.ParcelService/UpdateTrackingStatus
```

**Response: TrackingDataDto**
```
{
  string tracking_number = 1;
  repeated TrackingStampDto tracking_stamps = 2;
}
```

Example:
```
{
  "tracking_number": "TRACK12345678",
  "tracking_stamps": [
    {
      "tracking_status": "DELIVERED",
      "timestamp": "2024-06-24T15:30:00Z"
    },
    {
      "tracking_status": "IN_PROGRESS",
      "timestamp": "2024-06-23T10:00:00Z"
    }
  ]
}
```

`GetTrackingDataByTrackingNumber` - Retrieves the tracking data for a parcel.

**Request: TrackingNumberDto**
```
string tracking_number = 1 [(validate.rules).string.pattern = "TRACK[1-9]{8}$"];
```

Example:
```
grpcurl -plaintext -d '{
  "tracking_number": "TRACK12345678"
}' localhost:8080 parcel.ParcelService/GetTrackingDataByTrackingNumber

```

**Response: TrackingDataDto** the structure of this dto you can find above.

```
{
  "tracking_number": "TRACK12345678",
  "tracking_stamps": [
    {
      "tracking_status": "IN_PROGRESS",
      "timestamp": "2024-06-25T09:15:00Z"
    },
    {
      "tracking_status": "DELIVERED",
      "timestamp": "2024-06-26T13:45:00Z"
    }
  ]
}
```
## Troubleshooting
- Ensure that all dependencies are installed and up to date.
- Verify the service is running on the correct port (8000 by default).
- Check console logs for error messages and stack traces.

## Additional Resources

- [gRPC Documentation](https://grpc.io/docs/)
- [Protocol Buffers Documentation](https://developers.google.com/protocol-buffers/docs/overview)
- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)

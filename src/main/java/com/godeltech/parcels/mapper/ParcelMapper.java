package com.godeltech.parcels.mapper;

import com.godeltech.grpc.notification.SendNotificationDto;
import com.godeltech.grpc.parcel.*;
import com.godeltech.grpc.user.UserDto;
import com.godeltech.grpc.user.UserIdDto;
import com.godeltech.parcels.model.Parcel;
import com.godeltech.parcels.model.TrackingStamp;
import com.google.protobuf.Timestamp;
import java.time.Instant;
import java.util.List;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    collectionMappingStrategy = CollectionMappingStrategy.TARGET_IMMUTABLE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ParcelMapper {

  ParcelDto parcelToParcelDto(Parcel parcel);

  Parcel parcelDtoToParcel(ParcelDto parcelDto);

  Parcel createParcelDtoToParcel(CreateParcelDto createParcelDto);

  @Mapping(source = "parcelList", target = "parcelDtosList")
  ParcelDtoList parcelListToParcelDtoList(int count, List<Parcel> parcelList);

  @Mapping(source = "trackingStamps", target = "trackingStampsList")
  TrackingDataDto toTrackingDataDto(List<TrackingStamp> trackingStamps, String trackingNumber);

  @Mapping(source = "epochSecond", target = "seconds")
  @Mapping(source = "nano", target = "nanos")
  Timestamp instantMapToTimestamp(Instant timestamp);

  TrackingStamp toTrackingStamp(TrackingStatus trackingStatus, Instant timestamp);

  @Mapping(target = "allFields", ignore = true)
  @Mapping(target = "userDto", source = "userDto")
  SendNotificationDto toSendNotificationDto(UserDto userDto, String message);

  UserIdDto toUserIdDto(Long id);
}

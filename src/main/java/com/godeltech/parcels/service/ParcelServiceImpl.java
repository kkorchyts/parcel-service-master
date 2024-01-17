package com.godeltech.parcels.service;

import static com.godeltech.grpc.notification.NotificationServiceGrpc.NotificationServiceBlockingStub;
import static com.godeltech.grpc.parcel.TrackingStatus.CANCELLED;
import static com.godeltech.grpc.parcel.TrackingStatus.REGISTERED;

import com.github.javafaker.service.FakeValuesService;
import com.godeltech.grpc.parcel.*;
import com.godeltech.grpc.parcel.ParcelServiceGrpc.ParcelServiceImplBase;
import com.godeltech.grpc.user.UserIdDto;
import com.godeltech.grpc.user.UserServiceGrpc.UserServiceBlockingStub;
import com.godeltech.parcels.advice.ParcelNotFoundException;
import com.godeltech.parcels.mapper.ParcelMapper;
import com.godeltech.parcels.model.Parcel;
import com.godeltech.parcels.repository.ParcelRepository;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.lognet.springboot.grpc.GRpcService;
import validation.ProtoValidationService;

@GRpcService
@RequiredArgsConstructor
public class ParcelServiceImpl extends ParcelServiceImplBase {

  private final ParcelMapper parcelMapper;
  private final ParcelRepository parcelRepository;
  private final FakeValuesService fakeValuesService;
  private final ProtoValidationService validationService;

  @GrpcClient("notification-service")
  private NotificationServiceBlockingStub notificationServiceStub;

  @GrpcClient("user-service")
  private UserServiceBlockingStub userServiceStub;

  @Override
  public void createParcel(
      final CreateParcelDto request, final StreamObserver<ParcelDto> responseObserver) {
    validationService.validate(request);
    var userDto =
        userServiceStub.findUserById(UserIdDto.newBuilder().setId(request.getUserId()).build());
    var parcel = parcelMapper.createParcelDtoToParcel(request);
    parcel.addTrackingStamp(parcelMapper.toTrackingStamp(REGISTERED, Instant.now()));
    parcel.setTrackingNumber(fakeValuesService.regexify("TRACK[1-9]{8}"));
    notificationServiceStub.sendNotification(
        parcelMapper.toSendNotificationDto(
            userDto,
            "The new parcel is"
                + REGISTERED
                + ". The tracking number: "
                + parcel.getTrackingNumber()
                + "."));
    responseObserver.onNext(parcelMapper.parcelToParcelDto(parcelRepository.save(parcel)));
    responseObserver.onCompleted();
  }

  @Override
  public void findParcelByTrackingNumber(
      final TrackingNumberDto request, final StreamObserver<ParcelDto> responseObserver) {
    validationService.validate(request);
    var trackingNumber = request.getTrackingNumber();
    var parcel = getParcelOrElseThrowException(trackingNumber);
    responseObserver.onNext(parcelMapper.parcelToParcelDto(parcel));
    responseObserver.onCompleted();
  }

  @Override
  public void deleteParcelByTrackingNumber(
      final TrackingNumberDto request, final StreamObserver<Empty> responseObserver) {
    validationService.validate(request);
    parcelRepository.deleteById(request.getTrackingNumber());
    responseObserver.onNext(Empty.getDefaultInstance());
    responseObserver.onCompleted();
  }

  @Override
  public void cancelParcelByTrackingNumber(
      final TrackingNumberDto request, final StreamObserver<Empty> responseObserver) {
    validationService.validate(request);
    var trackingNumber = request.getTrackingNumber();
    var parcel = getParcelOrElseThrowException(trackingNumber);
    parcel.addTrackingStamp(parcelMapper.toTrackingStamp(CANCELLED, Instant.now()));
    parcelRepository.save(parcel);
    var userDto = userServiceStub.findUserById(parcelMapper.toUserIdDto(parcel.getUserId()));
    notificationServiceStub.sendNotification(
        parcelMapper.toSendNotificationDto(
            userDto, "Your parcel (tracking number: " + trackingNumber + ") was " + CANCELLED));
    responseObserver.onNext(Empty.getDefaultInstance());
    responseObserver.onCompleted();
  }

  @Override
  public void updateParcel(
      final ParcelDto request, final StreamObserver<ParcelDto> responseObserver) {
    validationService.validate(request);
    var trackingNumber = request.getTrackingNumber();
    getParcelOrElseThrowException(trackingNumber);
    var updatedParcel = parcelRepository.save(parcelMapper.parcelDtoToParcel(request));
    responseObserver.onNext(parcelMapper.parcelToParcelDto(updatedParcel));
    responseObserver.onCompleted();
  }

  @Override
  public void updateTrackingStatus(
      final UpdateTrackingStatusDto request,
      final StreamObserver<TrackingDataDto> responseObserver) {
    validationService.validate(request);
    var trackingNumber = request.getTrackingNumber();
    var parcel = getParcelOrElseThrowException(trackingNumber);
    var newStatus = request.getNewStatus();
    parcel.addTrackingStamp(parcelMapper.toTrackingStamp(newStatus, Instant.now()));
    var updatedParcel = parcelRepository.save(parcel);
    var userDto = userServiceStub.findUserById(parcelMapper.toUserIdDto(updatedParcel.getUserId()));
    notificationServiceStub.sendNotification(
        parcelMapper.toSendNotificationDto(
            userDto,
            "The parcel's status (tracking number: "
                + trackingNumber
                + ") was changed to "
                + newStatus));
    responseObserver.onNext(
        parcelMapper.toTrackingDataDto(updatedParcel.getTrackingStamps(), trackingNumber));
    responseObserver.onCompleted();
  }

  @Override
  public void getTrackingDataByTrackingNumber(
      final TrackingNumberDto request, final StreamObserver<TrackingDataDto> responseObserver) {
    validationService.validate(request);
    var trackingNumber = request.getTrackingNumber();
    var parcel = getParcelOrElseThrowException(trackingNumber);
    responseObserver.onNext(
        parcelMapper.toTrackingDataDto(parcel.getTrackingStamps(), trackingNumber));
    responseObserver.onCompleted();
  }

  @Override
  public void findAllParcel(
      final Empty request, final StreamObserver<ParcelDtoList> responseObserver) {
    var parcelList = parcelRepository.findAll();
    responseObserver.onNext(parcelMapper.parcelListToParcelDtoList(parcelList.size(), parcelList));
    responseObserver.onCompleted();
  }

  private Parcel getParcelOrElseThrowException(String trackingNumber) {
    return parcelRepository
        .findById(trackingNumber)
        .orElseThrow(() -> new ParcelNotFoundException(trackingNumber));
  }
}

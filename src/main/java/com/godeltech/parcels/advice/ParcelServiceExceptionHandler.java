package com.godeltech.parcels.advice;

import static io.grpc.Status.INVALID_ARGUMENT;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.lognet.springboot.grpc.recovery.GRpcExceptionHandler;
import org.lognet.springboot.grpc.recovery.GRpcExceptionScope;
import org.lognet.springboot.grpc.recovery.GRpcServiceAdvice;
import validation.ProtoValidationException;

@GRpcServiceAdvice
public class ParcelServiceExceptionHandler {
  @GRpcExceptionHandler
  public Status handleStatusRuntimeException(
      final StatusRuntimeException statusRuntimeException,
      final GRpcExceptionScope gRpcExceptionScope) {
    return statusRuntimeException.getStatus().withDescription(statusRuntimeException.getMessage());
  }

  @GRpcExceptionHandler
  public Status handleProtoValidationException(
      final ProtoValidationException exception, final GRpcExceptionScope gRpcExceptionScope) {
    return INVALID_ARGUMENT.withDescription(exception.getMessage());
  }
}

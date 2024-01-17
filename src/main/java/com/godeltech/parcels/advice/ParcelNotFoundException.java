package com.godeltech.parcels.advice;

public class ParcelNotFoundException extends RuntimeException {
  public ParcelNotFoundException(String trackingNumber) {
    super("The parcel with the tracking number: " + trackingNumber + " not found");
  }
}

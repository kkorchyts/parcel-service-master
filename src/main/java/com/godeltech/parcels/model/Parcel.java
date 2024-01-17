package com.godeltech.parcels.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "parcels", schema = "parcel_service")
@Data
public class Parcel {

  @Id
  @Column(unique = true, nullable = false)
  private String trackingNumber;

  @Column(nullable = false)
  private Long userId;

  @Column(nullable = false)
  private Double length;

  @Column(nullable = false)
  private Double width;

  @Column(nullable = false)
  private Double height;

  @Column(nullable = false)
  private Double weight;

  @Column(nullable = false)
  private String destination;

  @OneToMany(
      mappedBy = "parcel",
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  @OrderBy("timestamp")
  private List<TrackingStamp> trackingStamps;

  public void addTrackingStamp(final TrackingStamp trackingStamp) {
    if (Objects.isNull(trackingStamps)) trackingStamps = new ArrayList<>();
    trackingStamps.add(trackingStamp);
    trackingStamp.setParcel(this);
  }
}

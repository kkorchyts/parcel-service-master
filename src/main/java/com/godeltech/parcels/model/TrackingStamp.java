package com.godeltech.parcels.model;

import com.godeltech.grpc.parcel.TrackingStatus;
import java.time.Instant;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tracking", schema = "parcel_service")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrackingStamp {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "parcel_service.tracking_id_seq")
  private Long id;

  @Enumerated(EnumType.STRING)
  private TrackingStatus trackingStatus;

  private Instant timestamp;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "tracking_number", nullable = false)
  private Parcel parcel;
}

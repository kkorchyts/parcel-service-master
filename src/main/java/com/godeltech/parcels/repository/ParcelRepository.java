package com.godeltech.parcels.repository;

import com.godeltech.parcels.model.Parcel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParcelRepository extends JpaRepository<Parcel, String> {}

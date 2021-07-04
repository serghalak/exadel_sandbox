package com.exadel.sandbox.repository.vendor;

import com.exadel.sandbox.model.vendorinfo.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface VendorRepository extends JpaRepository<Vendor, Long>, VendorRepositoryCustom {

    @Query(value = "SELECT v.* FROM vendor v " +
            "JOIN event e on e.vendor_id=v.id " +
            "JOIN saved_event se on se.event_id=e.id " +
            "WHERE se.user_id = ?1", nativeQuery = true)
    Set<Vendor> getAllCategoriesFromSaved(Long userId);
}

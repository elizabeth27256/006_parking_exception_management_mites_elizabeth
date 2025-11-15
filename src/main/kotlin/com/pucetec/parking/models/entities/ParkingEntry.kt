package com.pucetec.parking.models.entities

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "parking_entries")
data class ParkingEntry(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, unique = true)
    val plate: String,

    @Column(nullable = false)
    val ownerName: String,

    @Column(nullable = false)
    val entryTime: LocalDateTime = LocalDateTime.now()
)

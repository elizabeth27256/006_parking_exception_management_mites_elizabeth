package com.pucetec.parking.controllers

import com.pucetec.parking.models.entities.ParkingEntry
import com.pucetec.parking.services.ParkingService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/parking/entries")
class ParkingController(
    private val parkingService: ParkingService
) {

    @PostMapping
    fun registerEntry(@RequestBody entry: ParkingEntry): ParkingEntry {
        return parkingService.registerEntry(entry)
    }

    @GetMapping("/{plate}")
    fun getByPlate(@PathVariable plate: String): ParkingEntry {
        return parkingService.getByPlate(plate)
    }

    @DeleteMapping("/{plate}")
    fun removeByPlate(@PathVariable plate: String) {
        parkingService.removeByPlate(plate)
    }
}

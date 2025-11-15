package com.pucetec.parking.services

import com.pucetec.parking.exception.*
import com.pucetec.parking.models.entities.ParkingEntry
import com.pucetec.parking.repositories.ParkingRepository
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.LocalDateTime

@Service
class ParkingService(
    private val repository: ParkingRepository
) {

    private val blacklistedPlates = listOf(
        "AAA-0001",
        "BBB-0002"
    )

    private val plateRegex = Regex("^[A-Z]{3}-\\d{4}$")

    fun registerEntry(entry: ParkingEntry): ParkingEntry {

        // Si ya hay 20 autos → error
        if (repository.count() >= 20) {
            throw ParkingFullException("El parqueadero ya está lleno")
        }

        // Placa inválida
        if (!plateRegex.matches(entry.plate)) {
            throw InvalidPlateFormatException("La placa ${entry.plate} no cumple con el formato AAA-1234")
        }

        // Lista negra
        if (blacklistedPlates.contains(entry.plate)) {
            throw BlacklistedPlateException("La placa ${entry.plate} está en lista negra")
        }

        // Ya está parqueado
        if (repository.existsByPlate(entry.plate)) {
            throw CarAlreadyParkedException("El auto con placa ${entry.plate} ya está registrado")
        }

        return repository.save(entry)
    }

    fun getByPlate(plate: String): ParkingEntry {
        return repository.findByPlate(plate)
            ?: throw CarNotFoundException("No existe un auto con la placa $plate")
    }

    fun removeByPlate(plate: String) {
        val entry = repository.findByPlate(plate)
            ?: throw CarNotFoundException("No existe un auto con la placa $plate")

        val hours = Duration.between(entry.entryTime, LocalDateTime.now()).toHours()

        if (hours > 8) {
            throw ParkingTimeExceededException("El vehículo ha excedido las 8 horas permitidas")
        }

        repository.delete(entry)
    }
}

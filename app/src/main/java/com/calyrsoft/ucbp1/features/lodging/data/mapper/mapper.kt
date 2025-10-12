package com.calyrsoft.ucbp1.features.lodging.data.mapper

import com.calyrsoft.ucbp1.features.lodging.data.database.entity.LodgingEntity
import com.calyrsoft.ucbp1.features.lodging.domain.model.Lodging
import com.calyrsoft.ucbp1.features.lodging.domain.model.LodgingType

fun LodgingEntity.toModel() = Lodging(
    id = id,
    name = name,
    type = LodgingType.valueOf(type),
    district = district,
    address = address,
    contactPhone = contactPhone,
    open24h = open24h,
    ownerAdminId = ownerAdminId,
    latitude = latitude,
    longitude = longitude,
    stayOptions = stayOptions,
    roomOptions = roomOptions,
    placeImageUri = placeImageUri,
    licenseImageUri = licenseImageUri
)

fun Lodging.toEntity() = LodgingEntity(
    id = id ?: 0,
    name = name,
    type = type.name,
    district = district,
    address = address,
    contactPhone = contactPhone,
    open24h = open24h,
    ownerAdminId = ownerAdminId,
    latitude = latitude,
    longitude = longitude,
    stayOptions = stayOptions,
    roomOptions = roomOptions,
    placeImageUri = placeImageUri,
    licenseImageUri = licenseImageUri
)



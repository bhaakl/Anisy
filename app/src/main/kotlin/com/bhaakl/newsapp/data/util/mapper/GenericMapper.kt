package com.bhaakl.newsapp.data.util.mapper

import org.mapstruct.InheritInverseConfiguration
import org.mapstruct.Mapper

@Mapper
interface GenericMapper<S, T> {

    fun mapToDto(domain: S): T

    @InheritInverseConfiguration
    fun mapToDomain(dto: T): S
}
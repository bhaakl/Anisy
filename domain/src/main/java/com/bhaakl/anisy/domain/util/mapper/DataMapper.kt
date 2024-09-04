package com.bhaakl.anisy.domain.util.mapper

interface DataMapper<T> {
    fun mapToDomain(): T
}
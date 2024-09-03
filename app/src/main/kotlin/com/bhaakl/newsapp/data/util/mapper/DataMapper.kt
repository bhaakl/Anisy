package com.bhaakl.newsapp.data.util.mapper

interface DataMapper<T> {
    fun mapToDomain(): T
}
package com.android.taitasciore.privaliatest.data.entity.mapper

interface Mapper<T, R> {
    fun map(input: T): R
}
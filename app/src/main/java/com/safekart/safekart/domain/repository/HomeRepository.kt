package com.safekart.safekart.domain.repository

import com.safekart.safekart.data.model.HomeData

interface HomeRepository {

    suspend fun getHome(): Result<HomeData>
}

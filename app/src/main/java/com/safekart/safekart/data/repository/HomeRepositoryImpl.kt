package com.safekart.safekart.data.repository

import com.safekart.safekart.data.model.HomeData
import com.safekart.safekart.data.remote.home.HomeRemoteDataSource
import com.safekart.safekart.domain.repository.HomeRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepositoryImpl @Inject constructor(
    private val homeRemoteDataSource: HomeRemoteDataSource
) : HomeRepository {

    override suspend fun getHome(): Result<HomeData> {
        return homeRemoteDataSource.getHome()
    }
}

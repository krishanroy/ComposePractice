package com.krishan.composePractice.pricelineprep.data.repo.spacex

import com.krishan.composePractice.pricelineprep.data.di.SpacexRetrofit
import com.krishan.composePractice.pricelineprep.data.remote.apicontract.SpacexLauncherService
import com.krishan.composePractice.pricelineprep.data.remote.dto.launches.SpacexLaunchDto
import com.krishan.composePractice.pricelineprep.data.remote.dto.launches.toDomain
import com.krishan.composePractice.pricelineprep.domain.model.SpacexLaunch
import com.krishan.composePractice.pricelineprep.domain.repo.SpacexRepository
import com.krishan.composePractice.pricelineprep.domain.repo.safeApiFlow
import com.krishan.composePractice.pricelineprep.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SpacexRepositoryImpl @Inject constructor(private val spacexService: SpacexLauncherService) : SpacexRepository {
    override suspend fun getAllLaunches(): Flow<Resource<List<SpacexLaunch>>> = safeApiFlow(
        apiCall = { spacexService.getAllSpaceXLaunches() },
        transform = { dto: List<SpacexLaunchDto> -> dto.map { it.toDomain() } }
    )
}
package com.tgirard12.krecyclerdsl

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface GitHubService {

    @GET("/search/repositories")
    fun search(@Query("q") name: String): Single<Repositories>

}
package com.line.saj.network

import com.line.saj.components.model.Memo
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers

interface ServerAPI {

    @Headers("Content-Type: application/json")
    @GET("/2020-flo/song.json")
    fun loadSong(
    ): Single<Memo>

}
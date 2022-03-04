package com.example.musicdao.core.usecases.releases

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.musicdao.core.model.Album
import com.example.musicdao.core.repositories.AlbumRepository
import javax.inject.Inject

class GetRelease @Inject constructor(private val albumRepository: AlbumRepository) {

    @RequiresApi(Build.VERSION_CODES.O)
    suspend operator fun invoke(id: String): Album {
        return albumRepository.get(id)
    }
}
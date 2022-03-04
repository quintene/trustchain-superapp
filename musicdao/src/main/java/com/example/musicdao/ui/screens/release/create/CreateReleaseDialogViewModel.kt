package com.example.musicdao.ui.screens.release.create

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.example.musicdao.core.usecases.CreateReleaseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateReleaseDialogViewModel @Inject constructor(private val createReleaseUseCase: CreateReleaseUseCase) :
    ViewModel() {

    @RequiresApi(Build.VERSION_CODES.O)
    fun createRelease(
        artist: String,
        title: String,
        releaseDate: String,
        uris: List<Uri>,
        context: Context
    ): Boolean {
        return createReleaseUseCase.invoke(artist, title, releaseDate, uris, context)
    }
}
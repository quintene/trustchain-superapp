package com.example.musicdao.ui.screens.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.musicdao.ui.components.EmptyState

@Composable
fun MyProfileScreen() {

    val ownProfileViewScreenModel: MyProfileScreenViewModel = hiltViewModel()
    val profile = ownProfileViewScreenModel.profile.collectAsState()

    profile.value?.let {
        Profile(it)
    } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        EmptyState(
            firstLine = "You have not made a profile yet.",
            secondLine = "Please make one first."
        )
    }

}

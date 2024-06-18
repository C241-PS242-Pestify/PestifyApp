package com.learning.pestifyapp.ui.components

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BottomBarState : ViewModel() {

    private val _bottomAppBarState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val bottomAppBarState: StateFlow<Boolean> get() = _bottomAppBarState.asStateFlow()

    fun setBottomAppBarState(state: Boolean) {
        Log.e("BottomBarState", "setBottomAppBarState: $state")
        viewModelScope.launch {
            _bottomAppBarState.value = state
        }
    }
}
package com.example.crudsops.data.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ItemViewModelFactory(): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ItemViewModel::class.java)){
            return ItemViewModel() as T
        }
        throw IllegalArgumentException ("UnknownViewModel")
    }
}
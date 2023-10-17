package com.example.crudsops.data

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName

data class Item(
    @get:Exclude var id: String? = null,
    @get:PropertyName("title") @set:PropertyName("title") var title: String = "",
    @get:PropertyName("description") @set:PropertyName("description") var description: String = ""
)
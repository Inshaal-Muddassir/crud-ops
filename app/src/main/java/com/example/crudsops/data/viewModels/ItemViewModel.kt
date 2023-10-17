package com.example.crudsops.data.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.crudsops.data.Item
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ItemViewModel : ViewModel() {
    private var db: FirebaseFirestore = Firebase.firestore
    private var auth: FirebaseAuth = Firebase.auth

    var lst = MutableLiveData<ArrayList<Item>>()

    init {
        db.collection("users")
            .document(auth.currentUser!!.uid)
            .collection("items")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    val items: ArrayList<Item> = ArrayList()
                    for (singleSnap in snapshot.documents) {
                        val item: Item? = singleSnap.toObject(Item::class.java)
                        item!!.id = singleSnap.id
                        items.add(item)
                        lst.value = items
                    }
                } else {
                    lst.value = ArrayList()
                }
            }
    }

    fun add(title: String, description: String) {
        val item = Item(null, title, description)

        // adding data to fire store
        db.collection("users")
            .document(auth.currentUser!!.uid)
            .collection("items")
            .add(item)
            .addOnSuccessListener {

            }
    }

    fun remove(item: Item) {
        item.id?.let {
            db.collection("users")
                .document(auth.currentUser!!.uid)
                .collection("items")
                .document(it)
                .delete()
                .addOnSuccessListener {

                }
        }
    }

    fun update(item: Item) {
        val updates = hashMapOf<String, Any>(
            "title" to item.title,
            "description" to item.description
        )

        item.id?.let {
            db.collection("users")
                .document(auth.currentUser!!.uid)
                .collection("items")
                .document(it)
                .update(updates)
        }
    }
}
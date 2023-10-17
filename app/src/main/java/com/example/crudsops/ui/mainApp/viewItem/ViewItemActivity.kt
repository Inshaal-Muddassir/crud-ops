package com.example.crudsops.ui.mainApp.viewItem

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.crudsops.databinding.ActivityViewItemBinding
import com.example.crudsops.data.viewModels.ItemViewModel
import com.example.crudsops.data.viewModels.ItemViewModelFactory

class ViewItemActivity : AppCompatActivity(), EditItemBottomSheet.CallbackListener {

    private lateinit var binding: ActivityViewItemBinding
    private lateinit var id: String
    private lateinit var title: String
    private lateinit var description: String

    private lateinit var itemViewModel: ItemViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // setting custom toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // initializing view model
        itemViewModel = ViewModelProvider(this, ItemViewModelFactory())[ItemViewModel::class.java]

        // fetching intent data
        id = intent.getStringExtra("id")!!
        title = intent.getStringExtra("title")!!
        description = intent.getStringExtra("description")!!

        // displaying data
        binding.tvItemTitle.text = title
        binding.tvItemDescription.text = description

        // initializing on click listeners
        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.btnEdit.setOnClickListener {
            val editItemBottomSheet =
                EditItemBottomSheet(0, itemViewModel, id, title, description, this)
            editItemBottomSheet.show(
                supportFragmentManager,
                editItemBottomSheet.javaClass.simpleName
            )
            editItemBottomSheet.isCancelable = false
        }
    }

    // call back from edit item bottom sheet
    override fun onDataChanged(title: String, description: String) {
        this.title = title
        this.description = description
        binding.tvItemTitle.text = title
        binding.tvItemDescription.text = description
    }
}
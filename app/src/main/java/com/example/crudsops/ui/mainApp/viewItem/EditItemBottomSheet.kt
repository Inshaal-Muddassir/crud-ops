package com.example.crudsops.ui.mainApp.viewItem

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.crudsops.Utils
import com.example.crudsops.databinding.SheetEditItemBinding
import com.example.crudsops.data.Item
import com.example.crudsops.data.viewModels.ItemViewModel
import com.google.android.material.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class EditItemBottomSheet(
    contentLayoutId: Int,
    private val viewModel: ItemViewModel,
    private val id: String,
    private val title: String,
    private val description: String,
    private val callbackListener: CallbackListener
) :
    BottomSheetDialogFragment(contentLayoutId) {

    private lateinit var context: Context
    private lateinit var binding: SheetEditItemBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.context = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SheetEditItemBinding.inflate(layoutInflater, container, false)
        dialog?.setOnShowListener { dialog ->
            val d = dialog as BottomSheetDialog
            val bottomSheetInternal =
                d.findViewById<View>(R.id.design_bottom_sheet)
            val behavior = BottomSheetBehavior.from(
                bottomSheetInternal!!
            )
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.skipCollapsed = true

            setBottomSheet()
        }
        return binding.root
    }

    private fun setBottomSheet() {
        // setting data
        binding.etTitle.setText(this.title)
        binding.etDescription.setText(this.description)

        // setting on click listeners
        binding.close.setOnClickListener {
            this.dismiss()
        }
        binding.done.setOnClickListener {
            val editedTitle = binding.etTitle.text.trim().toString()
            val editedDescription = binding.etDescription.text.trim().toString()
            if (editedTitle.isEmpty()) {
                Utils.displayToast(context, "Title is required", Toast.LENGTH_SHORT)
                return@setOnClickListener
            }
            if (editedDescription.isEmpty()) {
                Utils.displayToast(context, "Description is required", Toast.LENGTH_SHORT)
                return@setOnClickListener
            }
            if (editedTitle == title && editedDescription == description) {
                this.dismiss()
                return@setOnClickListener
            }

            val item = Item(id, editedTitle, editedDescription)

            viewModel.update(item)
            callbackListener.onDataChanged(editedTitle, editedDescription)
            this.dismiss()
        }
    }

    interface CallbackListener {
        fun onDataChanged(title: String, description: String)
    }

}
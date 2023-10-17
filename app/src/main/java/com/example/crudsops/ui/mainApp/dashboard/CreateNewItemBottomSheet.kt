package com.example.crudsops.ui.mainApp.dashboard

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.crudsops.Utils.displayToast
import com.example.crudsops.databinding.SheetCreateNewItemBinding
import com.example.crudsops.data.viewModels.ItemViewModel
import com.google.android.material.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CreateNewItemBottomSheet(contentLayoutId: Int, private val viewModel: ItemViewModel) :
    BottomSheetDialogFragment(contentLayoutId) {

    private lateinit var context: Context
    private lateinit var binding: SheetCreateNewItemBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.context = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SheetCreateNewItemBinding.inflate(layoutInflater, container, false)
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
        // setting on click listeners
        binding.close.setOnClickListener {
            this.dismiss()
        }
        binding.done.setOnClickListener {
            val itemTitle = binding.etTitle.text.trim().toString()
            val itemDescription = binding.etDescription.text.trim().toString()
            if (itemTitle.isEmpty()) {
                displayToast(context, "Title is required", Toast.LENGTH_SHORT)
                return@setOnClickListener
            }
            if (itemDescription.isEmpty()) {
                displayToast(context, "Description is required", Toast.LENGTH_SHORT)
                return@setOnClickListener
            }
            viewModel.add(itemTitle, itemDescription)
            this.dismiss()
        }
    }
}
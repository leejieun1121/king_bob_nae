package com.example.king_bob_nae.features.create.detail.presentaion.viewholder

import android.text.InputType
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.example.king_bob_nae.databinding.ItemDetailKkiLogIngredientBinding
import com.example.king_bob_nae.features.create.detail.domain.model.KkiLogIngredient
import com.example.king_bob_nae.features.create.detail.presentaion.DetailKkiLogViewModel

class IngredientViewHolder(
    private val binding: ItemDetailKkiLogIngredientBinding,
    detailKkiLogViewModel: DetailKkiLogViewModel
) : RecyclerView.ViewHolder(binding.root) {
    private lateinit var item: KkiLogIngredient

    init {
        binding.viewModel = detailKkiLogViewModel
        binding.etIngredient.doOnTextChanged { text, _, _, _ ->
            detailKkiLogViewModel.emptyIngredient.value = text.toString()
        }

        binding.etIngredient.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                if (binding.etIngredient.text.isEmpty()) {
                    detailKkiLogViewModel.updateIngredient(item)
                } else {
                    binding.etIngredient.apply {
                        clearFocus()
                        isFocusable = false
                        isEnabled = false
                        inputType = InputType.TYPE_NULL
                    }
                }
            }
        }
    }

    fun bind(item: KkiLogIngredient) {
        this.item = item
        binding.run {
            this.item = item
        }
    }
}

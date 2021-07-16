package dev.psuchanek.endurancepacecalculator.adapters.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.psuchanek.endurancepacecalculator.databinding.LayoutSplitsListItemBinding
import dev.psuchanek.endurancepacecalculator.models.UIModel

class SplitsViewHolder(private val binding: LayoutSplitsListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(uiSplitItem: UIModel.SplitsModel) {
        binding.splitsItem = uiSplitItem.splitItem
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): SplitsViewHolder {
            val binding = LayoutSplitsListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return SplitsViewHolder(binding)
        }
    }

}
package dev.psuchanek.endurancepacecalculator.adapters.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.psuchanek.endurancepacecalculator.databinding.LayoutZonesListItemBinding
import dev.psuchanek.endurancepacecalculator.models.zones.Zones

class ZonesViewHolder(private val binding: LayoutZonesListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(zoneItem: Zones) {
        binding.zoneItem = zoneItem
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): ZonesViewHolder {
            val binding = LayoutZonesListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return ZonesViewHolder(binding)
        }
    }

}
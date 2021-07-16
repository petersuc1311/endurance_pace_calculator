package dev.psuchanek.endurancepacecalculator.adapters.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.psuchanek.endurancepacecalculator.databinding.LayoutZonesListItemBinding
import dev.psuchanek.endurancepacecalculator.models.UIModel
import dev.psuchanek.endurancepacecalculator.models.zones.Zones

class ZonesViewHolder(private val binding: LayoutZonesListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(uiZoneItem: UIModel.ZonesModel) {
        binding.zoneItem = uiZoneItem.zonesItem
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
package dev.psuchanek.endurancepacecalculator.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.psuchanek.endurancepacecalculator.databinding.SplitsAndZonesListItemBinding
import dev.psuchanek.endurancepacecalculator.models.Zones
import timber.log.Timber

class ZonesListAdapter : ListAdapter<Zones, ZonesListAdapter.ZonesViewHolder>(diffUtil) {

    class ZonesViewHolder(private val binding: SplitsAndZonesListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(zoneItem: Zones) {
            binding.zoneItem = zoneItem
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ZonesViewHolder {
                val binding = SplitsAndZonesListItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return (ZonesViewHolder(binding))
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ZonesViewHolder {
        return ZonesViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ZonesViewHolder, position: Int) {
        val zoneItem = getItem(position)
        holder.bind(zoneItem)
    }
}

private val diffUtil = object : DiffUtil.ItemCallback<Zones>() {
    override fun areItemsTheSame(oldItem: Zones, newItem: Zones): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: Zones, newItem: Zones): Boolean {
        return oldItem == newItem
    }

}
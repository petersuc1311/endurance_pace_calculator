package dev.psuchanek.endurancepacecalculator.adapters

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.psuchanek.endurancepacecalculator.adapters.viewholders.SplitsViewHolder
import dev.psuchanek.endurancepacecalculator.adapters.viewholders.ZonesViewHolder
import dev.psuchanek.endurancepacecalculator.models.UIModel
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException

class EnduranceListAdapter() :
    ListAdapter<UIModel, RecyclerView.ViewHolder>(getDiffUtil()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ZONES_MODEL_TYPE -> {
                ZonesViewHolder.from(parent)
            }
            SPLITS_MODEL_TYPE -> {
                SplitsViewHolder.from(parent)
            }
            else -> throw IllegalStateException("This model type is not supported.")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is ZonesViewHolder -> {
                holder.bind(item as UIModel.ZonesModel)
            }
            is SplitsViewHolder -> {
                holder.bind(item as UIModel.SplitsModel)
            }
            else -> throw IllegalArgumentException("Model is not supported.")
        }
    }

    override fun getItemViewType(position: Int) = when (currentList[position]) {
        is UIModel.ZonesModel -> ZONES_MODEL_TYPE
        is UIModel.SplitsModel -> SPLITS_MODEL_TYPE
        else -> throw IllegalStateException("Unknown viewType")
    }

    companion object {
        const val ZONES_MODEL_TYPE = 0
        const val SPLITS_MODEL_TYPE = 1
    }


}

fun getDiffUtil() = object : DiffUtil.ItemCallback<UIModel>() {
    override fun areItemsTheSame(oldItem: UIModel, newItem: UIModel): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: UIModel, newItem: UIModel): Boolean {
        return oldItem == newItem
    }

}


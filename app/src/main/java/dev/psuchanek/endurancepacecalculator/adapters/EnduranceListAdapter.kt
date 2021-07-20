package dev.psuchanek.endurancepacecalculator.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.psuchanek.endurancepacecalculator.R
import dev.psuchanek.endurancepacecalculator.adapters.viewholders.SplitsViewHolder
import dev.psuchanek.endurancepacecalculator.adapters.viewholders.ZonesViewHolder
import dev.psuchanek.endurancepacecalculator.models.UIModel
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException

class EnduranceListAdapter(private val context: Context) :
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
                holder.apply {
                    bind(item as UIModel.ZonesModel)
                    setViewHolderColor(holder, getColorResource(position))
                }
            }
            is SplitsViewHolder -> {
                holder.apply {
                    bind(item as UIModel.SplitsModel)
                    changeViewHolderBackground(this, position)
                }
            }
            else -> throw IllegalArgumentException("Model is not supported.")
        }
    }

    private fun changeViewHolderBackground(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        if (position % 2 == 0) {
            setViewHolderColor(holder, R.color.lighter_gray)
        } else {
            setViewHolderColor(holder, R.color.white)
        }
    }

    private fun setViewHolderColor(holder: RecyclerView.ViewHolder, colorResource: Int) {
        holder.itemView.setBackgroundColor(
            context.resources.getColor(
                colorResource,
                null
            )
        )
    }

    private fun getColorResource(position: Int): Int {
        return when (position) {
            0 -> {
                R.color.zone_one_color
            }
            1 -> {
                R.color.zone_two_color
            }
            2 -> {
                R.color.zone_three_color
            }
            3 -> {
                R.color.zone_four_color
            }
            4 -> {
                R.color.zone_five_color
            }
            5 -> {
                R.color.zone_six_color
            }
            6 -> {
                R.color.zone_seven_color
            }
            else -> throw IllegalArgumentException("Color resource not found.")
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


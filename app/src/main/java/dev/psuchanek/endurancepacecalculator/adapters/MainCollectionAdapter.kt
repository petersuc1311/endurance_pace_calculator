package dev.psuchanek.endurancepacecalculator.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import dev.psuchanek.endurancepacecalculator.ui.pace.PaceCalculatorFragment
import dev.psuchanek.endurancepacecalculator.ui.splits.SplitsCalculatorFragment
import dev.psuchanek.endurancepacecalculator.ui.zones.ZonesCalculatorFragment
import dev.psuchanek.endurancepacecalculator.utils.FRAGMENT_MAIN_TAB_COUNT

class MainCollectionAdapter(fm: Fragment) : FragmentStateAdapter(fm) {
    override fun getItemCount() = FRAGMENT_MAIN_TAB_COUNT

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                PaceCalculatorFragment()
            }
            1 -> {
                ZonesCalculatorFragment()
            }
            2 -> {
                SplitsCalculatorFragment()
            }
            else -> {
                PaceCalculatorFragment()
            }
        }
    }
}
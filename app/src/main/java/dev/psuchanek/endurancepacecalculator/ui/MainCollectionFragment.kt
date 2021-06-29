package dev.psuchanek.endurancepacecalculator.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dev.psuchanek.endurancepacecalculator.R
import dev.psuchanek.endurancepacecalculator.adapters.MainCollectionAdapter
import dev.psuchanek.endurancepacecalculator.databinding.FragmentMainCollectionBinding

class MainCollectionFragment : Fragment(R.layout.fragment_main_collection) {

    private lateinit var binding: FragmentMainCollectionBinding
    private lateinit var mainTabLayout: TabLayout
    private lateinit var mainViewPager: ViewPager2
    private lateinit var mainCollectionAdapter: MainCollectionAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainCollectionBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTabsAndViewPager()
    }

    private fun initTabsAndViewPager() {
        mainViewPager = binding.viewPagerMain
        mainCollectionAdapter = MainCollectionAdapter(this)
        mainViewPager.adapter = mainCollectionAdapter
        mainTabLayout = binding.tabLayoutMain

        TabLayoutMediator(
            mainTabLayout,
            mainViewPager) { tab, position ->
            when(position) {
                0 -> {
                    tab.text = resources.getString(R.string.pace_calculator_tab_label)
                }
                1 -> {
                    tab.text = resources.getString(R.string.zones_calculator_tab_label)
                }
                2 -> {
                    tab.text = resources.getString(R.string.splits_calculator_tab_label)
                }
            }
        }.attach()
    }
}
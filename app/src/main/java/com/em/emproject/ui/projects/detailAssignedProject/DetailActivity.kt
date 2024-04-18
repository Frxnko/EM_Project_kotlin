package com.em.emproject.ui.projects.detailAssignedProject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.em.emproject.R
import com.em.emproject.databinding.ActivityDetailBinding
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    companion object{
        var CODE_PROJECT_DETAIL:String=""
        const val CODE_NODE: String = ""
    }

    private lateinit var binding: ActivityDetailBinding
    private lateinit var navController: NavController

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2
    private lateinit var adapter: FragmentPageAdapter

    private val args: DetailActivityArgs by navArgs()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initComponents()
        initUI()
    }

    private fun initComponents() {
        CODE_PROJECT_DETAIL=args.codeProject
        tabLayout = findViewById(R.id.tabLayoutPDetail)
        viewPager2 = findViewById(R.id.vp2Detail)


    }

    private fun initUI() {
        initListeners()
        binding.tvTitle.text = args.codeProject
        initNavigation()
    }

    private fun initListeners() {
        binding.btBackDetail.setOnClickListener { onBackPressed() }
    }


    private fun initNavigation() {
        adapter = FragmentPageAdapter(supportFragmentManager, lifecycle)
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.informationDetail)))
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.progressDetail)))
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.activitiesDetail)))
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.moreDetail)))

        viewPager2.adapter = adapter

        tabLayout.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.position?.let { viewPager2.setCurrentItem(it,false) }
                if (tab != null) {
                    viewPager2.currentItem = tab.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
        binding.vp2Detail.isUserInputEnabled=false

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })

    }

}
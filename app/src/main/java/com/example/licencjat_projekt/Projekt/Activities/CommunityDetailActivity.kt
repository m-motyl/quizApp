package com.example.licencjat_projekt.Projekt.Activities

import android.R
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import com.example.licencjat_projekt.Projekt.Models.GroupModel
import com.example.licencjat_projekt.databinding.ActivityCommunityDetailBinding
import kotlinx.android.synthetic.main.activity_community.*
import kotlinx.android.synthetic.main.activity_community_detail.*

class CommunityDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityCommunityDetailBinding

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityCommunityDetailBinding.bind(layoutInflater)
//        setContentView(binding.root)
//        community_detail_toolbar.setNavigationOnClickListener {
//            onBackPressed()
//        }
//
//        val user = arrayOf("Jan", "Kuba", "Jacek")
//
//        val userAdapter: ArrayAdapter<String> = ArrayAdapter(
//            this, R.layout.simple_list_item_1, user
//        )
//
//        binding.communityDetailSearch.suggestionsAdapter = userAdapter
//
//        binding.communityDetailRecycle.setQueryTextListener(object : SearchView.OnQueryTextListener) {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                binding.searchView.clearFocus()
//                if (user.contains(query)) {
//                    userAdapter.filter.filter(query)
//                }
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                userAdapter.filter.filter(newText)
//                return false
//            }
//        }
//    }
}
package com.example.licencjat_projekt.Projekt.Activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.licencjat_projekt.R
import kotlinx.android.synthetic.main.activity_community_detail.*

class CommunityDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community_detail)
        community_detail_toolbar.setNavigationOnClickListener{
            onBackPressed()
        }
    }
}


//lateinit var binding: ActivityCommunityDetailBinding

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
//}
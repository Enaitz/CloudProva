package com.enaitzdam.cloudprova

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.enaitzdam.cloudprova.databases.UserViewModel
import com.enaitzdam.cloudprova.databinding.ActivityMainBinding
import com.enaitzdam.cloudprova.databinding.ActivityRvactivityBinding
import com.google.firebase.firestore.FirebaseFirestore


class RVActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRvactivityBinding



    private lateinit var viewModel : UserViewModel
    //private val authViewModel: AuthViewModel by viewModels()
    private lateinit var userRecyclerView: RecyclerView
    lateinit var adapter: MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_rvactivity)

        binding = ActivityRvactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userRecyclerView = binding.recyclerView
        userRecyclerView.layoutManager=LinearLayoutManager(applicationContext)
        userRecyclerView.setHasFixedSize(true)
        adapter = MyAdapter()
        userRecyclerView.adapter = adapter

        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        viewModel.allUsers.observe(this, Observer {

            adapter.updateUserList(it)

        })




    }
}
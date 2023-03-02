package com.enaitzdam.cloudprova.databases

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserViewModel : ViewModel(){

    private val repository : UserRepositori
    private val _allUsers = MutableLiveData<List<User>>()
    val allUsers : LiveData<List<User>> = _allUsers


    init {

        repository = UserRepositori().getInstance()
        repository.loadUsers(_allUsers)

    }
}
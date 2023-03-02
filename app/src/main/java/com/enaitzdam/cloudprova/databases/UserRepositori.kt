package com.enaitzdam.cloudprova.databases

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class UserRepositori {

    //private val databaseReference:DatabaseReference = FirebaseDatabase.getInstance().coll

    var db = FirebaseFirestore.getInstance()
    private lateinit var userArrayList: ArrayList<User>

    @Volatile
    private var INSTANCE: UserRepositori? = null

    fun getInstance(): UserRepositori {
        return INSTANCE ?: synchronized(this) {

            val instance = UserRepositori()
            INSTANCE = instance
            instance
        }


    }


    fun loadUsers(userList: MutableLiveData<List<User>>) {
        userArrayList = ArrayList<User>()
        db.collection("users")
            .orderBy("firstName", com.google.firebase.firestore.Query.Direction.ASCENDING)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }
                for (dc: DocumentChange in snapshots!!.documentChanges) {

                    when (dc.type) {
                      //DocumentChange.Type.ADDED -> Log.d(TAG, "New city: ${dc.document.data}")

                        DocumentChange.Type.ADDED -> userArrayList.add(dc.document.toObject(User::class.java))
                        DocumentChange.Type.MODIFIED -> userArrayList.add(dc.document.toObject(User::class.java))
                        DocumentChange.Type.REMOVED -> userArrayList.add(dc.document.toObject(User::class.java))
                    }
                }
                userList.postValue(userArrayList)
            }

    }


}


//    fun loadUsers(userList : MutableLiveData<List<User>>){
//
//        databaseReference.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//
//                try {
//
//                    val _userList : List<User> = snapshot.children.map { dataSnapshot ->
//
//                        dataSnapshot.getValue(User::class.java)!!
//
//                    }
//
//                    userList.postValue(_userList)
//
//                }catch (e : Exception){
//
//
//                }
//
//
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//
//
//        })
//
//
//    }



package com.enaitzdam.cloudprova

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.enaitzdam.cloudprova.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding

    // creating a storage reference
    val storageRef = Firebase.storage.reference;

    private var currentFile: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth=Firebase.auth

        auth.signInWithEmailAndPassword("enaitz@gmail.com", "1234567890")
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    Toast.makeText(this,"Connected$user",Toast.LENGTH_SHORT).show()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()

                }
            }

        binding.buttonSelect.setOnClickListener {
            selectImage()
        }

        binding.buttonUpload.setOnClickListener{
            uploadImageToStroage("1")
        }
        binding.buttonNext.setOnClickListener{
            val intent = Intent(applicationContext, RVActivity::class.java)
            startActivity(intent)
        }


    }

    private fun selectImage() {
        Intent(Intent.ACTION_GET_CONTENT).also {
            it.type = "image/*"
            imageLauncher.launch(it)
        }
    }

    private val imageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.data.let {
                    currentFile = it
                    binding.imageView.setImageURI(currentFile)
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    "Fatal error carregant imatge!!",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

    private fun uploadImageToStroage(filename: String) {
        try {
            currentFile?.let {
                //De moment pujo a la carpeta images del Storage Cloud
                storageRef.child("images/$filename").putFile(it)
                    .addOnSuccessListener {
                        Toast.makeText(applicationContext, "Arxiu pujat!!", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(applicationContext, "Error pujant l'arxiu", Toast.LENGTH_SHORT).show()
                    }
            }
        } catch (e: Exception) {
            Toast.makeText(applicationContext, "Error${e.toString()}", Toast.LENGTH_SHORT).show()
        }
    }
}


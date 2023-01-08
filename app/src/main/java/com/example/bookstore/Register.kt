package com.example.bookstore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.bookstore.databinding.ActivityRegisterBinding
import com.example.bookstore.ui.login
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class register : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()

        val spinner: Spinner = findViewById(R.id.spinner_state)
        ArrayAdapter.createFromResource(
            this,
            R.array.states_array,
            android.R.layout.simple_spinner_item
        )
            .also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                spinner.adapter = adapter
            }


        binding.textView.setOnClickListener {
            val intent = Intent(this, login::class.java)
            startActivity(intent)
        }
        binding.btnRegister.setOnClickListener {
            val email = binding.emailRegister.text.toString()
            val pass = binding.pwdRegister.text.toString()
            val fName = binding.ttFname.text.toString()
            val lName = binding.ttLname.text.toString()
            val address = binding.ttAddress.text.toString()
            val state = binding.spinnerState.selectedItem.toString()
            val city = binding.ttCity.text.toString().toString()
            val zipcode = binding.ttZipcode.text.toString().toInt()
            val confirmPass = binding.registerRetypepwd.text.toString()


            database = FirebaseDatabase.getInstance().getReference("User")

            val User = User(fName, lName, address, state, email, city,zipcode)
            database.child(fName).setValue(User).addOnSuccessListener {
                binding.ttFname.text?.clear()
                binding.ttLname.text?.clear()
                binding.ttAddress.text?.clear()
                binding.emailRegister.text?.clear()
                binding.spinnerState.clearFocus()
                binding.ttCity.text?.clear()
                binding.ttZipcode.text?.clear()
                Toast.makeText(this, "Registered Successfully", Toast.LENGTH_SHORT).show()

            }.addOnFailureListener {
                Toast.makeText(this,"Failed To Register",Toast.LENGTH_SHORT).show()
            }


            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()) {
                if (pass == confirmPass) {
                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val intent = Intent(this, login::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }

                } else {
                    Toast.makeText(this, "Password is not matching", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Empty Fields are not Allowed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        TODO("Not yet implemented")
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}




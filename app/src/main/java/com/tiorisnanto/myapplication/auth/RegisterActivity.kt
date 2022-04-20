package com.tiorisnanto.myapplication.auth

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.auth.User
import com.tiorisnanto.myapplication.MainActivity
import com.tiorisnanto.myapplication.R
import com.tiorisnanto.myapplication.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var ref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        ref = FirebaseDatabase.getInstance().getReference("USERS")

        binding.btnSignUp.setOnClickListener {
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()
            val confirmPassword = binding.edtConfirmPassword.text.toString()
            val name = binding.edtName.text.toString()
            val phone = binding.edtPhone.text.toString()

            if (TextUtils.isEmpty(email)) {
                binding.edtEmail.error = "Email is required"
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.edtEmail.error = "Invalid email"
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(password)) {
                binding.edtPassword.error = "Password is required"
                return@setOnClickListener
            }

            if (password.length < 6) {
                binding.edtPassword.error = "Password must be at least 6 characters"
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(confirmPassword)) {
                binding.edtConfirmPassword.error = "Confirm password is required"
                return@setOnClickListener
            }

            if (confirmPassword != password) {
                binding.edtConfirmPassword.error = "Password doesn't match"
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(name)) {
                binding.edtName.error = "Name is required"
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(phone)) {
                binding.edtPhone.error = "Phone is required"
                return@setOnClickListener
            }

            registrasi(email, password, name, phone)
        }

    }

    private fun registrasi(email: String, password: String, name: String, phone: String) {

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val progressDialog = ProgressDialog(this)
                    progressDialog.setTitle("Register")
                    progressDialog.setMessage("Please wait...")
                    progressDialog.setCanceledOnTouchOutside(false)
                    progressDialog.show()

                    ref.child(auth.currentUser!!.uid).child("name").setValue(name)
                    val firebaseUser = auth.currentUser
                    val email = firebaseUser!!.email
                    Toast.makeText(this, "Account Created with email ${email}", Toast.LENGTH_SHORT)
                        .show()

                    //open profile
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(
                        this,
                        "Failed to create user: " + task.exception!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }


}
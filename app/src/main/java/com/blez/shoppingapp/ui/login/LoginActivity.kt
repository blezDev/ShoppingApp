package com.blez.shoppingapp.ui.login

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.blez.shoppingapp.MainActivity
import com.blez.shoppingapp.R
import com.blez.shoppingapp.databinding.ActivityLoginBinding
import com.blez.shoppingapp.util.TokenManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var googleSigninClient : GoogleSignInClient
    private lateinit var alert : androidx.appcompat.app.AlertDialog
   private lateinit var tokenManager : TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login)
        tokenManager = TokenManager(this@LoginActivity)
        auth = FirebaseAuth.getInstance()
        val gson = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSigninClient = GoogleSignIn.getClient(this,gson)
         alert = androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Process is going on!!")
            .setMessage("Please Wait for few moments.")
            .setCancelable(false)
            .create()
        binding.googleBTN.setOnClickListener {
            alert.show()
            sigInGoogle()
        }

        if (!tokenManager.getEmail().isNullOrEmpty()){
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
    private fun sigInGoogle(){
        val siginInIntent = googleSigninClient.signInIntent
        launcher.launch(siginInIntent)

    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result->
        if(result.resultCode == Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleResults(task)
        }
    }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if(task.isSuccessful){
            val account:GoogleSignInAccount?  = task.result
            if(account!= null){
                updateUI(account)
            }
        }
        else {
            Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
        }

    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken,null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if(it.isSuccessful) {
                account.email?.let { it1 -> tokenManager.saveEmail(it1) }
                account.givenName?.let { it-> tokenManager.saveGivenName(it) }
                account.photoUrl?.let { it->tokenManager.savePic(it.toString()) }
                tokenManager.saveUserName(account.email?.replace("@gmail.com","")?.trim()!!)
                tokenManager.saveToken(account.idToken.toString())
                Log.e("TAG",tokenManager.getPic().toString())
                alert.dismiss()
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()



            }else {
                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                Log.e("TAG",it.exception.toString())
                alert.dismiss()
            }
        }

    }


}
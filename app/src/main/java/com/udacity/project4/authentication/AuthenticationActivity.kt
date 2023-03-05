package com.udacity.project4.authentication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthMethodPickerLayout
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.AuthUI.IdpConfig.*
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.udacity.project4.R
import com.udacity.project4.locationreminders.RemindersActivity
import kotlinx.android.synthetic.main.activity_authentication.*
import java.util.*


/**
 * This class should be the starting point of the app, It asks the users to sign in / register, and redirects the
 * signed in users to the RemindersActivity.
 */
class AuthenticationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)
//         TODO: Implement the create account and sign in using FirebaseUI, use sign in using email and sign in using Google

//          TODO: If the user was authenticated, send him to RemindersActivity
        checkIfLogedIn()
        login.setOnClickListener {
            startSignIn()
        }
//          TODO: a bonus is to customize the sign in flow to look nice using :
        //https://github.com/firebase/FirebaseUI-Android/blob/master/auth/README.md#custom-layout

    }

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { result: FirebaseAuthUIAuthenticationResult? ->
        checkIfLogedIn()
    }

    private fun checkIfLogedIn() {
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            val intent = Intent(this, RemindersActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            // not signed in
        }
    }

    // ...
    private fun startSignIn() {

        // You must provide a custom layout XML resource and configure at least one
// provider button ID. It's important that that you set the button ID for every provider
// that you have enabled.
        // You must provide a custom layout XML resource and configure at least one
// provider button ID. It's important that that you set the button ID for every provider
// that you have enabled.
        val customLayout = AuthMethodPickerLayout.Builder(R.layout.activity_login)
            .setGoogleButtonId(R.id.googleLogin)
            .setEmailButtonId(R.id.email) // ...
            .build()
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(
                listOf(
                    GoogleBuilder().build(),
                    // FacebookBuilder().build(),
                    //TwitterBuilder().build(),
                    //MicrosoftBuilder().build(),
                    //YahooBuilder().build(),
                    /// AppleBuilder().build(),
                    EmailBuilder().build()
                    //   PhoneBuilder().build(),
                    //  AnonymousBuilder().build()
                )
            )
            .setAuthMethodPickerLayout(customLayout)
            .build()

        signInLauncher.launch(signInIntent)

    }
}

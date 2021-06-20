package app.java.mystlyo.view

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import app.java.mystlyo.databinding.ActivityMainBinding
import app.java.mystlyo.viewmodel.MainViewModel
import com.facebook.*
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.common.api.Status
import com.google.android.gms.tasks.Task
import org.json.JSONException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*


class MainActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 100
    var binding: ActivityMainBinding? = null
    var viewmodel: MainViewModel? = null
    var mGoogleSignInClient : GoogleSignInClient? = null
    var callbackmanager : CallbackManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        viewmodel = ViewModelProvider(this).get(MainViewModel::class.java)

        printHashKey()

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("319864290467-n068ah0naghf6bmf4um80btrr88dsaqe.apps.googleusercontent.com")
            .requestEmail()
            .requestProfile()
            .build()

        FacebookSdk.sdkInitialize(getApplicationContext());

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        binding?.btnGoogle?.setOnClickListener {
            //viewmodel?.googleLogin()
            val signInIntent = mGoogleSignInClient?.getSignInIntent()
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        binding?.btnFacebook?.setOnClickListener {
            Fblogin()
        }
    }

    override fun onStart() {
        super.onStart()
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if(account != null){
            googleLogOut()
        }else{
            //new sign in
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }else{
            callbackmanager?.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            updateUI(account)
        } catch (e: ApiException) {
            Log.w("TAG", "signInResult:failed code=" + e.statusCode)
            updateUI(null)
        }
    }

    private fun updateUI(account: GoogleSignInAccount?) {
        print("${account?.email} -- ${account?.photoUrl}")
        val intent = Intent(this, ProfileActivity::class.java)
        intent.putExtra("url", account?.photoUrl.toString())
        startActivity(intent)
    }

    private fun googleLogOut(){
        mGoogleSignInClient?.signOut()
    }

    fun printHashKey() {
        try {
            val info: PackageInfo = getPackageManager()
                .getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val hashKey = String(Base64.encode(md.digest(), 0))
                Log.i("TAG", "printHashKey() Hash Key: $hashKey")
            }
        } catch (e: NoSuchAlgorithmException) {
            Log.e("TAG", "printHashKey()", e)
        } catch (e: Exception) {
            Log.e("TAG", "printHashKey()", e)
        }
    }

    private fun Fblogin() {
        callbackmanager = CallbackManager.Factory.create()

        LoginManager.getInstance()
            .logInWithReadPermissions(this, Arrays.asList("email", "user_photos", "public_profile", "user_videos", "user_posts"))
        LoginManager.getInstance().registerCallback(callbackmanager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    println("Success")
                    GraphRequest.newMeRequest(
                        loginResult.accessToken
                    ) { json, response ->
                        if (response.error != null) {
                            // handle error
                            println("ERROR")
                        } else {
                            println("Success")
                            try {
                                val jsonresult = json.toString()
                                println("JSON Result$jsonresult")
                                val str_email = json.getString("email")
                                val str_id = json.getString("id")
                                val str_firstname = json.getString("first_name")
                                val str_lastname = json.getString("last_name")
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                        }
                    }.executeAsync()
                }

                override fun onCancel() {
                    Log.d("TAG", "On cancel")
                }

                override fun onError(error: FacebookException) {
                    Log.d("TAG", error.toString())
                }
            })
    }
}


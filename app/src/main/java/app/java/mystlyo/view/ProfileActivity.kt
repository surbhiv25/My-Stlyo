package app.java.mystlyo.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import app.java.mystlyo.databinding.ActivityProfileBinding
import app.java.mystlyo.view.adapter.ViewPagerAdapter
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.HttpMethod
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.squareup.picasso.Picasso


class ProfileActivity : AppCompatActivity() {

    var binding: ActivityProfileBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        loadProfileImage()
        setupAdapter()

        binding?.appCompatImageView2?.setOnClickListener{
            googleSignOut()
            disconnectFromFacebook()
        }
    }

    fun googleSignOut(){
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        googleSignInClient.signOut()
    }

    fun disconnectFromFacebook() {
        if (AccessToken.getCurrentAccessToken() == null) {
            return  // already logged out
        }
        GraphRequest(
            AccessToken.getCurrentAccessToken(),
            "/me/permissions/",
            null,
            HttpMethod.DELETE
        ) { LoginManager.getInstance().logOut() }
            .executeAsync()
    }

    private fun loadProfileImage() {
        val photoUrl = intent.getStringExtra("url").toString()
        Picasso.get().load(photoUrl).into(binding?.shapeableImageView)
    }

    private fun setupAdapter(){
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(VideosFragment(), "Videos")
        adapter.addFragment(ProductFragment(), "Products")
        binding?.viewPager?.adapter = adapter
        binding?.tabs?.setupWithViewPager(binding?.viewPager)
    }
}
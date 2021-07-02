package com.tech.imagesharingapp

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var imageUrl: String? = null
    var apiUrl: String? = null
    var text: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        load()
    }

    private fun load() {
        progress_bar.visibility = View.VISIBLE
        // Instantiate the RequestQueue.
//        val queue = Volley.newRequestQueue(this)
        apiUrl = "https://meme-api.herokuapp.com/gimme"

        // Request a string response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, apiUrl, null,
            Response.Listener { response ->
                imageUrl = response.getString("url")
                text = response.getString("title")
                textView.text=text
                Glide.with(this).load(imageUrl).listener(object : RequestListener<Drawable> {

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progress_bar.visibility = View.GONE
                        Toast.makeText(this@MainActivity, "failed to load image", Toast.LENGTH_LONG)
                            .show()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progress_bar.visibility = View.GONE
                        return false
                    }
                }).into(imageView)//image is being loaded here
            },
            Response.ErrorListener {
                Log.d("error", it.localizedMessage)
            })

// Add the request to the RequestQueue.
//        queue.add(jsonObjectRequest)
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }


    fun loadImages(view: View) {
        load()
    }

    fun shareImage(view: View) {
        //share the plain text
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(
            Intent.EXTRA_TEXT,
            "i have got this image from this amazing app " + imageUrl
        )
        startActivity(Intent.createChooser(intent, "share this text via.."))

    }


}
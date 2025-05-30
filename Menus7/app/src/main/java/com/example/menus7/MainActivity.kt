package com.example.menus7

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.TouristGuide5.DestinationListActivity
import com.example.TouristGuide5.DetailsActivity
import com.example.TouristGuide5.R
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {

    lateinit var progressBar: ProgressBar
    lateinit var webView: WebView
    lateinit var videoView: VideoView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        val btnNextPage: Button = findViewById(R.id.btn_next_page)
        val btnDestinationList: Button = findViewById(R.id.btn_destination_list)

        progressBar = findViewById(R.id.progress_bar)
        webView = findViewById(R.id.web_view)
        videoView = findViewById(R.id.video_view)

        // Fix WebView: Enable JavaScript & Load URL
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        webView.loadUrl("https://www.getyourguide.com/-l163462/?cmp=ga&cq_src=google_ads&cq_cmp=18660774938&cq_con=151148278028&cq_term=travel%20guia&cq_med=&cq_plac=&cq_net=g&cq_pos=&cq_plt=gp&campaign_id=18660774938&adgroup_id=151148278028&target_id=kwd-1577668618268&loc_physical_ms=9061998&match_type=b&ad_id=629857739005&keyword=travel%20guia&ad_position=&feed_item_id=&placement=&device=c&partner_id=CD951&gad_source=1&gclid=CjwKCAiAn9a9BhBtEiwAbKg6fjqLFPhi8-KoZaPjXQ8BbH7utBWOs0wpscSi6NKLh8qHQDUwXGyIuRoC4g4QAvD_BwE")

        // Fix VideoView: Load from raw folder
        val videoPath = "android.resource://" + packageName + "/" + R.raw.sample_vdo
        videoView.setVideoURI(Uri.parse(videoPath))
        videoView.setOnPreparedListener { it.start() }

        // Alert Dialog on FAB Click
        fab.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Information")
                .setMessage("Tourist Guide and Destination Recommender is a user-friendly mobile application designed to enhance travel experiences. It helps users discover popular destinations, get personalized recommendations, and easily access essential travel information, all in one place.")
                .setPositiveButton("OK", null)
                .show()
        }

        // Navigate to DetailsActivity
        btnNextPage.setOnClickListener {
            startActivity(Intent(this, DetailsActivity::class.java))
        }


        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = android.graphics.Color.TRANSPARENT

        // Navigate to DestinationListActivity
        btnDestinationList.setOnClickListener {
            startActivity(Intent(this, DestinationListActivity::class.java))
        }
    }
}

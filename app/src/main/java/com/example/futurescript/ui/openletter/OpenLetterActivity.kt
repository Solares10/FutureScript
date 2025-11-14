package com.example.futurescript.ui.openletter

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.example.futurescript.R
import com.google.android.material.textview.MaterialTextView

class OpenLetterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_letter)

        val videoView = findViewById<VideoView>(R.id.envelopeVideo)
        val messageText = findViewById<MaterialTextView>(R.id.letterMessage)

        val message = intent.getStringExtra("message") ?: "A message from your future self 💌"
        messageText.text = message

        // Play the envelope video stored in res/raw
        val videoUri = Uri.parse("android.resource://$packageName/${R.raw.envelope_open}")
        videoView.setVideoURI(videoUri)
        videoView.start()

        // Fade in the message after 3 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            messageText.animate().alpha(1f).setDuration(1000).start()
        }, 3000)

        // Close activity after 8 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            finish()
        }, 8000)
    }
}

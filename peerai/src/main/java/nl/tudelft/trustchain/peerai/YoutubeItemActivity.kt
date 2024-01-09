package nl.tudelft.trustchain.peerai

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class YoutubeItemActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube_item)


        // Change title of activity
        val title = intent.getStringExtra("title")
        if (title != null) {
            this.title = title
        }

        val artist = intent.getStringExtra("artist")


        val youtubeTitleTextView = findViewById<TextView>(R.id.youtube_title)
        youtubeTitleTextView.text = title

        val youtubeArtistTextView = findViewById<TextView>(R.id.youtube_artist)
        youtubeArtistTextView.text = artist

        // Set youtube id from passed param putExtra
        val youtubeId = intent.getStringExtra("id")


        // Create onclick for button and move to other activity
        val button = findViewById<Button>(R.id.back_button)
        button.setOnClickListener {
            finish()
        }

        // pass videoId to youtube player view to play video
        val youTubePlayerView: YouTubePlayerView = this.findViewById(R.id.youtube_player_view)
        // pass videoId to youTubePlayerView to play video
        youTubePlayerView.getYouTubePlayerWhenReady(object : YouTubePlayerCallback {
            override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                // do stuff with it
                if (youtubeId != null) {
                    youTubePlayer.loadVideo(youtubeId, 0f)
                }
            }
        })
    }
}

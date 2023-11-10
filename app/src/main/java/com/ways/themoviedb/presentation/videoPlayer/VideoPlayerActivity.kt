package com.ways.themoviedb.presentation.videoPlayer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.ways.themoviedb.databinding.ActivityVideoPlayerBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class VideoPlayerActivity : AppCompatActivity() {

    private val binding by lazy { ActivityVideoPlayerBinding.inflate(layoutInflater) }
    private val videoID by lazy { intent?.getStringExtra(KEY_INTENT_VIDEO_PLAYER_ID) ?: "" }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupView()
    }

    override fun onDestroy() {
        super.onDestroy()
        with(binding.videoPlayer) {
            release()
            removeYouTubePlayerListener(videoPlayerListener)
        }
    }

    private fun setupView() {
        with(binding) {
            lifecycle.addObserver(videoPlayer)
            videoPlayer.addYouTubePlayerListener(videoPlayerListener)
        }
    }

    private val videoPlayerListener = object : YouTubePlayerListener {
        override fun onApiChange(youTubePlayer: YouTubePlayer) = Unit

        override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) = Unit

        override fun onError(youTubePlayer: YouTubePlayer, error: PlayerConstants.PlayerError) {
            Timber.e("videoPlayer: is error $error")
        }

        override fun onPlaybackQualityChange(
            youTubePlayer: YouTubePlayer,
            playbackQuality: PlayerConstants.PlaybackQuality
        ) = Unit

        override fun onPlaybackRateChange(
            youTubePlayer: YouTubePlayer,
            playbackRate: PlayerConstants.PlaybackRate
        ) = Unit

        override fun onReady(youTubePlayer: YouTubePlayer) {
            Timber.d("videoPlayer: is ready")
            youTubePlayer.loadVideo(videoID, 0f)
        }

        override fun onStateChange(
            youTubePlayer: YouTubePlayer,
            state: PlayerConstants.PlayerState
        ) = Unit

        override fun onVideoDuration(youTubePlayer: YouTubePlayer, duration: Float) = Unit

        override fun onVideoId(youTubePlayer: YouTubePlayer, videoId: String) = Unit

        override fun onVideoLoadedFraction(youTubePlayer: YouTubePlayer, loadedFraction: Float) =
            Unit

    }

    companion object {
        const val KEY_INTENT_VIDEO_PLAYER_ID = "key_intent_video_player_id"
    }
}
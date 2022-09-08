package com.example.staj.feature.detail.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.staj.databinding.ItemTrailerBinding
import com.example.staj.models.Video
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.DefaultPlayerUiController

class TrailerAdapter(
    private var videos: List<Video>
) : RecyclerView.Adapter<TrailerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTrailerBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        holder.bind(position)
    }

    override fun getItemCount() = videos.size

    inner class ViewHolder(private val binding: ItemTrailerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val iFramePlayerOptions = IFramePlayerOptions.Builder()
            .controls(0)
            .build()

        fun bind(position: Int) {
            val mVideo = videos[position]

            val listener = object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    val defaultPlayerUiController =
                        DefaultPlayerUiController(binding.youtubePlayer, youTubePlayer)
                    defaultPlayerUiController.showMenuButton(false)
                    defaultPlayerUiController.showVideoTitle(false)
                    defaultPlayerUiController.showFullscreenButton(false)
                    binding.youtubePlayer.setCustomPlayerUi(defaultPlayerUiController.rootView)

                    youTubePlayer.cueVideo(mVideo.key, 0f)
                }
            }
            binding.trailerName.text = mVideo.name
            binding.youtubePlayer.initialize(listener, iFramePlayerOptions)
        }
    }
}

package com.ways.themoviedb.presentation.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ways.themoviedb.R
import com.ways.themoviedb.data.remote.response.genre.Genre
import com.ways.themoviedb.databinding.ItemGenreBinding
import com.ways.themoviedb.presentation.utils.DiffUtilAdapter

class GenreAdapter : RecyclerView.Adapter<GenreAdapter.ViewHolder>() {

    private var genres = listOf<Genre>()
    private var checkedItemPosition = 0

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = ItemGenreBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(genres[position])
    }

    override fun getItemCount(): Int = genres.size

    inner class ViewHolder(private val binding: ItemGenreBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(genre: Genre) {
            with(binding) {
                tvGenre.text = genre.name

                if (checkedItemPosition == -1) {
                    cardView.setCardBackgroundColor(
                        ContextCompat.getColor(
                            root.context,
                            android.R.color.transparent
                        )
                    )
                    tvGenre.setTextColor(
                        ContextCompat.getColor(
                            root.context,
                            R.color.white
                        )
                    )
                } else {
                    cardView.setCardBackgroundColor(
                        when (checkedItemPosition) {
                            absoluteAdapterPosition -> ContextCompat.getColor(
                                root.context,
                                R.color.background_soft
                            )

                            else -> ContextCompat.getColor(
                                root.context,
                                android.R.color.transparent
                            )
                        }
                    )
                    tvGenre.setTextColor(
                        when (checkedItemPosition) {
                            absoluteAdapterPosition -> ContextCompat.getColor(
                                root.context,
                                R.color.blue_primary
                            )

                            else -> ContextCompat.getColor(root.context, R.color.white)
                        }
                    )
                }

                // action on click
                root.setOnClickListener {
                    cardView.setCardBackgroundColor(
                        ContextCompat.getColor(
                            root.context,
                            R.color.background_soft
                        )
                    )
                    tvGenre.setTextColor(
                        ContextCompat.getColor(
                            root.context,
                            R.color.blue_primary
                        )
                    )

                    if (checkedItemPosition != absoluteAdapterPosition) {
                        notifyItemChanged(checkedItemPosition)
                        checkedItemPosition = absoluteAdapterPosition
                    }

                    onContentClickListener?.let { it1 -> it1(getSelectedGenre()) }
                }
            }
        }
    }

    fun setData(newGenres: List<Genre>) {
        val diffUtil = DiffUtilAdapter(genres, newGenres)
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        genres = newGenres
        diffResults.dispatchUpdatesTo(this)
    }

    private fun getSelectedGenre(): Genre {
        return if (checkedItemPosition != -1) {
            genres[checkedItemPosition]
        } else genres.first()
    }

    fun setSelectedGenre(genre: Genre) {
        checkedItemPosition = genres.indexOfFirst { it.id == genre.id }
        notifyItemChanged(checkedItemPosition)
    }

    private var onContentClickListener: ((Genre) -> Unit)? = null

    fun setOnContentClickListener(listener: (Genre) -> Unit) {
        onContentClickListener = listener
    }

}
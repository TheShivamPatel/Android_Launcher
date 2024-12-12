package com.studynotes.mylauncher.fragments.feeds.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.studynotes.mylauncher.R
import com.studynotes.mylauncher.databinding.NewsCardItemBinding
import com.studynotes.mylauncher.fragments.feeds.model.Article
import com.studynotes.mylauncher.viewUtils.ViewUtils.formatDate

class FeedAdapter(private val articleList : List<Article>) : RecyclerView.Adapter<FeedAdapter.FeedViewHolder>(){

    inner class FeedViewHolder(private val binding: NewsCardItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Article){
            binding.textView4.text = article.title
            if(article.content == null){
                binding.textView4.maxLines = 3
                binding.textView5.visibility = View.GONE
            }else{
                binding.textView5.text = article.content
                binding.textView5.visibility = View.VISIBLE
            }
            binding.textView6.text = article.publishedAt?.let { formatDate(it) }
            binding.shapeableImageView.load(article.urlToImage){
                crossfade(true)
                placeholder(R.drawable.place_holder_img)
                error(R.drawable.place_holder_img)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val view = NewsCardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FeedViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        holder.bind(articleList[position])
    }

    override fun getItemCount(): Int = articleList.size
}
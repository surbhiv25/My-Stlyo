package app.java.mystlyo.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.java.mystlyo.databinding.ItemVideoBinding

class VideoListAdapter(private val imageList: MutableList<Int>)
    : RecyclerView.Adapter<VideoListAdapter.ViewHolder?>() {

    inner class ViewHolder(val binding: ItemVideoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemVideoBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(imageList[position]){
                binding.imageView.setImageResource(this)
            }
        }
    }

    override fun getItemCount(): Int {
        return imageList.size
    }
}
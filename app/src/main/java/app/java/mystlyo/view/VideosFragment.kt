package app.java.mystlyo.view

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import app.java.mystlyo.R
import app.java.mystlyo.databinding.FragmentVideosBinding
import app.java.mystlyo.view.adapter.VideoListAdapter


class VideosFragment : Fragment() {

    var binding: FragmentVideosBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVideosBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListAdapter()
    }

    private fun setupListAdapter() {
        val imageList = mutableListOf(
            R.drawable.first, R.drawable.second, R.drawable.third,
            R.drawable.first, R.drawable.second, R.drawable.third
        )

        binding?.recyclerView?.layoutManager = GridLayoutManager(activity, 2, RecyclerView.VERTICAL, false)
        binding?.recyclerView?.addItemDecoration(object : ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                val position = parent.getChildAdapterPosition(view) // item position
                val spanCount = 2
                val spacing = 10 //spacing between views in grid
                if (position >= 0) {
                    val column = position % spanCount // item column
                    outRect.left =
                        spacing - column * spacing / spanCount // spacing - column * ((1f / spanCount) * spacing)
                    outRect.right =
                        (column + 1) * spacing / spanCount // (column + 1) * ((1f / spanCount) * spacing)
                    if (position < spanCount) { // top edge
                        outRect.top = spacing
                    }
                    outRect.bottom = spacing // item bottom
                } else {
                    outRect.left = 0
                    outRect.right = 0
                    outRect.top = 0
                    outRect.bottom = 0
                }
            }
        })
        binding?.recyclerView?.adapter = VideoListAdapter(imageList)
    }
}
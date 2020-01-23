package com.beloushkin.unphotos.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.beloushkin.unphotos.R
import com.beloushkin.unphotos.extensions.loadNetworkImage
import com.beloushkin.unphotos.model.Photo
import com.beloushkin.unphotos.util.getProgressDrawable
import kotlinx.android.synthetic.main.fragment_photo.*
import kotlinx.android.synthetic.main.item_photo.view.*

class PhotoListAdapter(
    private val photoList: ArrayList<Photo>
): RecyclerView.Adapter<PhotoListAdapter.PhotoViewHolder>() {

    fun updatePhotosList(newList: List<Photo>) {
        photoList.clear()
        photoList.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_photo,parent, false)
        return PhotoViewHolder(view)
    }

    override fun getItemCount() = photoList.size

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val currPhoto = photoList[position]
        bind(holder.view, currPhoto)
    }

    private fun bind(view:View, data:Photo) {
        view.photoDescription.text = data.description
        view.photoAuthor.text = data.user?.username
        view.photoImage.loadNetworkImage(data.url?.regular, getProgressDrawable(view.context),doNothing)

        view.photoLayout.setOnClickListener {
            val action = ListFragmentDirections.actionDetail(data)
            Navigation.findNavController(view).navigate(action)
        }
    }

    class PhotoViewHolder(var view: View): RecyclerView.ViewHolder(view)

    private var doNothing: (intColor: Int) -> Unit = {}
}
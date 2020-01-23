package com.beloushkin.unphotos.view


import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import com.beloushkin.unphotos.R
import com.beloushkin.unphotos.extensions.loadNetworkImage
import com.beloushkin.unphotos.extensions.saveNetworkImageToFileAsync
import com.beloushkin.unphotos.model.Photo
import com.beloushkin.unphotos.model.User
import com.beloushkin.unphotos.util.getProgressDrawable
import kotlinx.android.synthetic.main.fragment_photo.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions
import java.io.File

private const val LOCAL_AUTHORITY =  "com.beloushkin.unphotos.fileprovider"

@RuntimePermissions
class PhotoFragment : Fragment(),View.OnClickListener {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    var photo: Photo? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_photo, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bind(arguments)

        fabShare.setOnClickListener(this)
    }

    private fun bind(arguments: Bundle?) {
        // bind data
        arguments?.let {

            photo = PhotoFragmentArgs.fromBundle(it).photo
            context?.let { cont ->
                loadFullImage(photo, cont)
                tvAuthor.text = photo?.user?.username
                tvDescription.text = photo?.description
                photo?.user?.let { user ->
                    loadUserImage(user, cont)
                }
            }

        }
    }

    private var usePalette: (intColor: Int) -> Unit = {
        tvAuthor.setTextColor(it)
        tvDescription.setTextColor(it)
    }

    private var doNothing: (intColor: Int) -> Unit = {}

    private fun loadFullImage(photo: Photo?, context: Context) {
        photo?.let {
            fullImage.loadNetworkImage(photo.url?.regular, getProgressDrawable(context), usePalette)
        }
    }

    private fun loadUserImage(user: User?, context: Context) {
        user?.let {
            userAvatar.loadNetworkImage(it.profileImage?.small, getProgressDrawable(context),doNothing)
        }

    }


    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    override fun onClick(v: View?) {
        if (v?.id == fabShare.id) {

            context?.let {currContext ->
                coroutineScope.launch {
                   shareCurrentImage(currContext)
                }
            }
        }
    }

    private suspend fun saveCurrentImageToTmpFileAsync(context: Context, url:String):File? {

        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "share_image_" + System.currentTimeMillis() + ".png")
        return fullImage.saveNetworkImageToFileAsync(url,file).await()

    }

    private suspend fun shareCurrentImage(context: Context) {

        val tmpFile = saveCurrentImageToTmpFileAsync(context, photo!!.url!!.regular!!)
        tmpFile?.let { imageFile ->
            val bmpUri = FileProvider.getUriForFile(context,
                LOCAL_AUTHORITY, imageFile)
            val intent = Intent().apply {
                this.action = Intent.ACTION_SEND
                this.putExtra(Intent.EXTRA_STREAM, bmpUri)
                this.type = getString(R.string.image_type_string)
            }
            startActivity(Intent.createChooser(intent, resources.getText(R.string.send_to)))
        }
    }
}

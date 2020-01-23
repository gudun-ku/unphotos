package com.beloushkin.unphotos.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.beloushkin.unphotos.R
import com.beloushkin.unphotos.model.Photo
import com.beloushkin.unphotos.viewmodel.UnsplashViewModel
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.item_photo.*
import kotlinx.coroutines.*
import okhttp3.Dispatcher


class ListFragment : Fragment() {

    private lateinit var viewModel: UnsplashViewModel
    private val listAdapter = PhotoListAdapter(arrayListOf())
    private val listDataObserver = Observer<List<Photo>> { list ->
        list?.let {
            photosList.visibility = View.VISIBLE
            listAdapter.updatePhotosList(it)
        }
    }

    private val loadingDataObserver = Observer<Boolean> { isLoading ->
        loadingView.visibility = if (isLoading) View.VISIBLE else View.GONE
        if (isLoading) {
            listError.visibility = View.GONE
            photosList.visibility = View.GONE
        }
    }

    private val errorObserver = Observer<Boolean> { isError ->
        listError.visibility = if (isError) View.VISIBLE else View.GONE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(UnsplashViewModel::class.java)
        viewModel.photos.observe(this, listDataObserver)
        viewModel.isLoading.observe(this, loadingDataObserver)
        viewModel.loadError.observe(this, errorObserver)
        viewModel.refresh()

        photosList.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = listAdapter
        }

        refreshLayout.setOnRefreshListener {

            photosList.visibility = View.GONE
            listError.visibility = View.GONE
            loadingView.visibility = View.VISIBLE

            refreshLayout.isRefreshing = false
            viewModel.refresh()


        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

}

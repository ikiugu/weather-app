package com.ikiugu.weather.ui.favorites

/**
 * Created by Alfred Ikiugu on 09/06/2021
 */


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ikiugu.weather.R
import com.ikiugu.weather.databinding.FavoritesFragmentBinding
import com.ikiugu.weather.databinding.FavoritesListItemBinding
import com.ikiugu.weather.domain.ScreenWeather

class FavoritesFragment : Fragment() {

    companion object {
        fun newInstance() = FavoritesFragment()
    }

    private lateinit var favoritesViewModel: FavoritesViewModel

    private var viewModelAdapter: FavoritesAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FavoritesFragmentBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.favorites_fragment,
            container,
            false
        )

        favoritesViewModel = ViewModelProvider(this).get(FavoritesViewModel::class.java)

        viewModelAdapter = FavoritesAdapter()

        binding.root.findViewById<RecyclerView>(R.id.favorites_recycler).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewModelAdapter
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoritesViewModel.favoriteWeather.observe(viewLifecycleOwner) { screens ->
            screens.apply {
                viewModelAdapter?.screenWeather = screens
            }
        }
    }

}

class FavoritesAdapter : RecyclerView.Adapter<FavoritesDataViewHolder>() {
    var screenWeather: List<ScreenWeather> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesDataViewHolder {
        val withDataBinding: FavoritesListItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            FavoritesDataViewHolder.LAYOUT,
            parent,
            false
        )
        return FavoritesDataViewHolder(withDataBinding)
    }

    override fun getItemCount() = screenWeather.size

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
     * position.
     */
    override fun onBindViewHolder(holder: FavoritesDataViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.screenWeatherItem = screenWeather[position]
        }
    }

}

class FavoritesDataViewHolder(val viewDataBinding: FavoritesListItemBinding) :
    RecyclerView.ViewHolder(viewDataBinding.root) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.favorites_list_item
    }
}
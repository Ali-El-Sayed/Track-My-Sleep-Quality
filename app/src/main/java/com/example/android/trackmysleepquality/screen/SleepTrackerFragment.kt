package com.example.android.trackmysleepquality.screen


import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.database.SleepDatabase
import com.example.android.trackmysleepquality.databinding.FragmentSleepTrackerBinding
import com.example.android.trackmysleepquality.screen.adapter.SleepNightLinearAdapter
import com.example.android.trackmysleepquality.screen.adapter.SleepNightListener
import com.example.android.trackmysleepquality.sleeptracker.SleepTrackerViewModel
import com.example.android.trackmysleepquality.sleeptracker.SleepTrackerViewModelFactory

/**
 * A fragment with buttons to record start and end times for sleep, which are saved in
 * a database. Cumulative data is displayed in a simple scrollable TextView.
 * (Because we have not learned about RecyclerView yet.)
 */
class SleepTrackerFragment : Fragment() {

    lateinit var binding: FragmentSleepTrackerBinding
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var linearLayoutManager: LinearLayoutManager

    /**
     * Called when the Fragment is ready to display content to the screen.
     *
     * This function uses DataBindingUtil to inflate R.layout.fragment_sleep_quality.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        // Get a reference to the binding object and inflate the fragment views.
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_sleep_tracker, container, false
        )

        binding.lifecycleOwner = this

        setHasOptionsMenu(true)

        // Create an instance of the ViewModel Factory.
        val context = requireActivity().application
        val dataSource = SleepDatabase.getInstance(context).sleepDatabaseDao
        val viewModelFactory = SleepTrackerViewModelFactory(dataSource, context)


        // Get a reference to the ViewModel associated with this fragment.
        val sleepTrackerViewModel =
            ViewModelProvider(this, viewModelFactory).get(SleepTrackerViewModel::class.java)


        // To use the View Model with data binding, you have to explicitly
        // give the binding object a reference to it.
        binding.sleepTrackerViewModel = sleepTrackerViewModel

        // Recycler View
        val adapter = SleepNightLinearAdapter(SleepNightListener { nightId ->
            sleepTrackerViewModel.onSleepNightClicked(nightId)
        })
        sleepTrackerViewModel.navigateToSleepDetail.observe(
            viewLifecycleOwner,
            Observer { nightId ->
                nightId?.let {
                    val bundle = Bundle().apply {
                        putLong("sleepNightKey", nightId)
                    }
                    val navigation =
                        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                    navigation.navigate(
                        R.id.action_sleep_tracker_fragment_to_sleepDetailFragment, bundle
                    )
                    sleepTrackerViewModel.onSleepDetailNavigated()
                }
            })


        binding.sleepList.adapter = adapter
        gridLayoutManager = GridLayoutManager(
            requireActivity(), 2, GridLayoutManager.VERTICAL, false
        )
        linearLayoutManager = LinearLayoutManager(
            requireActivity(), LinearLayoutManager.VERTICAL, false
        )
        binding.sleepList.layoutManager = gridLayoutManager


        // Update The RecyclerView Adapter
        sleepTrackerViewModel.nights.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })


        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.sleep_tracker_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (binding.sleepList.layoutManager == linearLayoutManager) {
            binding.sleepList.layoutManager = gridLayoutManager
            item.icon = context?.let { ContextCompat.getDrawable(it, R.drawable.ic_grid_layout) }
        } else {
            binding.sleepList.layoutManager = linearLayoutManager
            item.icon = context?.let { ContextCompat.getDrawable(it, R.drawable.ic_list_layout) }
        }
        return true
    }
}


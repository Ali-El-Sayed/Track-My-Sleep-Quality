package com.example.android.trackmysleepquality.screen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.databinding.ListItemSleepNightLinearBinding

class SleepNightAdapter(val clickListener: SleepNightListener) :
    ListAdapter<SleepNight, SleepNightAdapter.SleepTrackerViewHolder>(
        SleepNtDiffCallback()
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): SleepTrackerViewHolder {

        return SleepTrackerViewHolder.from(parent)
    }

    override fun onBindViewHolder(
        holder: SleepTrackerViewHolder, position: Int
    ) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }


    class SleepTrackerViewHolder private constructor(
        private val binding: ListItemSleepNightLinearBinding
    ) : RecyclerView.ViewHolder(
        binding.root
    ) {

        companion object {
            fun from(parent: ViewGroup): SleepTrackerViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemSleepNightLinearBinding.inflate(layoutInflater, parent, false)
                return SleepTrackerViewHolder(binding)
            }
        }

        fun bind(night: SleepNight, clickListener: SleepNightListener) {
            binding.sleep = night
            binding.clickListener = clickListener
            /**
             * Evaluates the pending bindings, updating any Views that have expressions bound to
             * modified variables. This <b>must</b> be run on the UI thread.
             */
            binding.executePendingBindings()
        }
    }
}

class SleepNtDiffCallback : DiffUtil.ItemCallback<SleepNight>() {
    override fun areItemsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean =
        oldItem.nightId == newItem.nightId


    override fun areContentsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean =
        oldItem == newItem
}

class SleepNightListener(
    val clickListener: (sleepId: Long) -> Unit
) {
    fun onClick(night: SleepNight) = clickListener(night.nightId)
}
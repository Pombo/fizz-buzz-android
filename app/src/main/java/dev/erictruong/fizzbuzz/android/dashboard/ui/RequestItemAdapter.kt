package dev.erictruong.fizzbuzz.android.dashboard.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.LinearProgressIndicator
import dev.erictruong.fizzbuzz.android.R
import dev.erictruong.fizzbuzz.android.dashboard.ui.model.RequestItem

/**
 * [RecyclerView.Adapter] that can display a [RequestItem].
 */
class RequestItemAdapter : ListAdapter<RequestItem, RequestItemViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_request, parent, false)
        return RequestItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: RequestItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    object DiffCallback : DiffUtil.ItemCallback<RequestItem>() {

        override fun areItemsTheSame(oldItem: RequestItem, newItem: RequestItem): Boolean {
            return oldItem.isSameItem(newItem)
        }

        override fun areContentsTheSame(oldItem: RequestItem, newItem: RequestItem): Boolean {
            return (oldItem == newItem)
        }
    }
}

class RequestItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val limits: TextView = view.findViewById(R.id.limits)
    private val requestCount: TextView = view.findViewById(R.id.request_count)

    private val numberOne: TextView = view.findViewById(R.id.number_one)
    private val wordOne: TextView = view.findViewById(R.id.word_one)
    private val wordOneHits: TextView = view.findViewById(R.id.word_one_hits)
    private val wordOneHitsIndicator: LinearProgressIndicator = view.findViewById(R.id.indicator_word_one_hits)

    private val numberTwo: TextView = view.findViewById(R.id.number_two)
    private val wordTwo: TextView = view.findViewById(R.id.word_two)
    private val wordTwoHits: TextView = view.findViewById(R.id.word_two_hits)
    private val wordTwoHitsIndicator: LinearProgressIndicator = view.findViewById(R.id.indicator_word_two_hits)

    private val wordBoth: TextView = view.findViewById(R.id.word_both)
    private val wordBothHits: TextView = view.findViewById(R.id.word_both_hits)
    private val wordBothHitsIndicator: LinearProgressIndicator = view.findViewById(R.id.indicator_word_both_hits)

    fun bind(item: RequestItem) {
        limits.text = item.limits
        requestCount.text = item.requestCount.toString()

        numberOne.text = item.numberOne.toString()
        wordOne.text = item.wordOne
        wordOneHits.text = item.wordOneHitsText
        wordOneHitsIndicator.apply {
            max = item.count
            progress = item.wordOneHits
        }

        numberTwo.text = item.numberTwo.toString()
        wordTwo.text = item.wordTwo
        wordTwoHits.text = item.wordTwoHitsText
        wordTwoHitsIndicator.apply {
            max = item.count
            progress = item.wordTwoHits
        }

        wordBoth.text = item.both
        wordBothHits.text = item.bothHitsText
        wordBothHitsIndicator.apply {
            max = item.count
            progress = item.bothHits
        }
    }
}
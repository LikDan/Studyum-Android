package com.ldc.kbp.views.adapters.timetable

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ldc.kbp.R
import com.ldc.kbp.models.Timetable
import com.ldc.kbp.views.adapters.Adapter
import kotlinx.android.synthetic.main.item_day_timetable.view.*

class TimetableAdapter(
    context: Context,
    var timetable: Timetable? = null
) : Adapter<Timetable.Day>(
    context,
    timetable?.weeks?.map { it.days }?.flatten() ?: listOf(),
    R.layout.item_day_timetable
) {
    private var recyclers: MutableList<RecyclerView> = mutableListOf()
    private var isReplacementShown = true

    override fun onBindViewHolder(view: View, item: Timetable.Day?, position: Int) {
        recyclers.add(view.item_timetable_day_recycler)

        view.item_timetable_day_recycler.foreground =
            if (item!!.state == Timetable.UpdateState.NOT_UPDATED)
                ColorDrawable(context.getColor(R.color.not_updated)) else null

        view.item_timetable_day_recycler.adapter =
            TimetableDayAdapter(
                context,
                if (isReplacementShown) item.replacementLessons else item.standardLessons,
                onLessonExpand
            )
    }

    var onLessonExpand: (Timetable.Lesson, Int) -> Unit = { _, _ -> }

    fun changeMode(): Boolean {
        isReplacementShown = !isReplacementShown

        items?.forEachIndexed { index, day ->
            if (day!!.state == Timetable.UpdateState.REPLACEMENT)
                notifyItemChanged(index)
        }

        return isReplacementShown
    }

    fun changeData(timetable: Timetable) {
        items = timetable.weeks.map { it.days }.flatten()
    }
}
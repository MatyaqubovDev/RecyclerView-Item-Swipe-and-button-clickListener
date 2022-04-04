package com.example.recyclerviewswipeanddelete

import android.content.Context
import android.graphics.Canvas
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerviewswipeanddelete.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val adapter by lazy { CardAdapter() }
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUI()
    }

    private fun setupUI() {
        var list = ArrayList<Card>()
        for (i in 0..10) {
            list.add(Card("Card$i"))
        }
        adapter.submitList(list)
        binding.recyclerView.adapter = adapter
        adapter.onDeleteClick = {
            Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show()
        }
        setItemTouchHelper()
    }

    private fun setItemTouchHelper() {

        ItemTouchHelper(object : ItemTouchHelper.Callback() {
            private val limitScrollX = dpToPx(
                100f,
                this@MainActivity
            ) // todo, the limit of swipe, same as the delete button in item, 100dp
            private var currentScrollX = 0
            private var currentScrollWhenInActive = 0
            private var initWhenInActive = 0f
            private var firstInActive = false

            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                val dragFlags = 0
                val swipeFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                return makeMovementFlags(dragFlags, swipeFlags)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

            }


            override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
                return Integer.MAX_VALUE.toFloat()
            }

            override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
                return Integer.MAX_VALUE.toFloat()
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    if (dX == 0f) {
                        currentScrollX = viewHolder.itemView.scrollX
                    }

                    if (isCurrentlyActive) {
                        var scrollOffset = currentScrollX + (-dX).toInt()
                        if (scrollOffset > limitScrollX) {
                            scrollOffset = limitScrollX
                        } else if (scrollOffset < 0) {
                            scrollOffset = 0
                        }

                        viewHolder.itemView.scrollTo(scrollOffset, 0)
                    } else {
                        //slide with auto anim
                        if (firstInActive) {
                            firstInActive = false
                            currentScrollWhenInActive = viewHolder.itemView.scrollX
                            initWhenInActive = dX
                        }

                        if (viewHolder.itemView.scrollX < limitScrollX) {
                            viewHolder.itemView.scrollTo(
                                (currentScrollWhenInActive * dX / initWhenInActive).toInt(),
                                0
                            )
                        }
                    }
                }
            }

            override fun clearView(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) {
                super.clearView(recyclerView, viewHolder)
                if (viewHolder.itemView.scrollX > limitScrollX) {
                    viewHolder.itemView.scrollTo(limitScrollX, 0)
                } else if (viewHolder.itemView.scrollX < 0) {
                    viewHolder.itemView.scrollTo(0, 0)
                }
            }

        }).apply {
            attachToRecyclerView(binding.recyclerView)
        }

//    private fun setItemTouchHelper() {
//        ItemTouchHelper(object : ItemTouchHelper.Callback() {
//            private val limitScrollX = dpToPx(100f, this@MainActivity)
//            private var currentScrollX = 0
//            private var currentScrollWhenActive = 0
//            private var initWhenActive = 0f
//            private var firstInActive = false
//            override fun getMovementFlags(
//                recyclerView: RecyclerView,
//                viewHolder: RecyclerView.ViewHolder
//            ): Int {
//                val dragFlags = 0
//                val swipeFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
//                return makeMovementFlags(dragFlags, swipeFlags)
//
//            }
//
//            override fun onMove(
//                recyclerView: RecyclerView,
//                viewHolder: RecyclerView.ViewHolder,
//                target: RecyclerView.ViewHolder
//            ): Boolean {
//                return true
//            }
//
//            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//
//            }
//
//            override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
//                return Integer.MAX_VALUE.toFloat()
//            }
//
//            override fun getSwipeVelocityThreshold(defaultValue: Float): Float {
//                return Integer.MAX_VALUE.toFloat()
//            }
//
//            override fun onChildDraw(
//                c: Canvas,
//                recyclerView: RecyclerView,
//                viewHolder: RecyclerView.ViewHolder,
//                dX: Float,
//                dY: Float,
//                actionState: Int,
//                isCurrentlyActive: Boolean
//            ) {
//                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
//                    if (dX == 0f) {
//                        currentScrollX = viewHolder.itemView.scrollX
//                    }
//                    if (isCurrentlyActive) {
//                        var scrollOffset = currentScrollX + (-dX).toInt()
//                        if (scrollOffset > limitScrollX) {
//                            scrollOffset = limitScrollX
//                        } else if (scrollOffset < 0) scrollOffset = 0
//                        viewHolder.itemView.scrollTo(scrollOffset, 0)
//                    } else {
//                        if (firstInActive) {
//                            firstInActive = false
//                            currentScrollWhenActive = viewHolder.itemView.scrollX
//                            initWhenActive = dX
//                        }
//                        if (viewHolder.itemView.scrollX < limitScrollX) {
//                            viewHolder.itemView.scrollTo(
//                                (currentScrollWhenActive * dX / initWhenActive).toInt(),
//                                0
//                            )
//                        }
//                    }
//                }
//            }
//
//            override fun clearView(
//                recyclerView: RecyclerView,
//                viewHolder: RecyclerView.ViewHolder
//            ) {
//                super.clearView(recyclerView, viewHolder)
//                if (viewHolder.itemView.scrollX > limitScrollX) {
//                    viewHolder.itemView.scrollTo(limitScrollX, 0)
//                } else {
//                    if (viewHolder.itemView.scrollX < 0) {
//                        viewHolder.itemView.scrollTo(0, 0)
//                    }
//                }
//            }
//
//        }).apply {
//            attachToRecyclerView(binding.recyclerView)
//        }
    }

    private fun dpToPx(dpValue: Float, context: Context): Int {
        return (dpValue * context.resources.displayMetrics.density).toInt()
    }
}
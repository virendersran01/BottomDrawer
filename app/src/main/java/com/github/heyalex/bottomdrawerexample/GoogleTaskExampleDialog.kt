package com.github.heyalex.bottomdrawerexample

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.SeekBar
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatSeekBar
import com.github.heyalex.bottomdrawer.BottomDrawerDialog
import com.github.heyalex.bottomdrawer.BottomDrawerFragment
import com.github.heyalex.handle.PlainHandleView
import com.github.heyalex.utils.changeNavigationIconColor
import com.github.heyalex.utils.changeStatusBarIconColor
import com.rtugeek.android.colorseekbar.ColorSeekBar

class GoogleTaskExampleDialog : BottomDrawerFragment() {

    private var alphaCancelButton = 0f
    private lateinit var cancelButton: ImageView

    private lateinit var cornerRadiusSeekBar: AppCompatSeekBar

    private lateinit var navigation: AppCompatCheckBox
    private lateinit var statusBar: AppCompatCheckBox
    private lateinit var colorSeekBar: ColorSeekBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.google_task_example_layout, container, false)
        cancelButton = view.findViewById(R.id.cancel)
        val percent = 0.65f
        addBottomSheetCallback {
            onSlide { _, slideOffset ->
                val alphaTemp = (slideOffset - percent) * (1f / (1f - percent))
                alphaCancelButton = if (alphaTemp >= 0) {
                    alphaTemp
                } else {
                    0f
                }
                cancelButton.alpha = alphaCancelButton
                cancelButton.isEnabled = alphaCancelButton > 0
            }
        }
        cancelButton.setOnClickListener { dismissWithBehavior() }

        cornerRadiusSeekBar = view.findViewById(R.id.corner_radius_seek_bar)
        cornerRadiusSeekBar.max = 200
        cornerRadiusSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                changeCornerRadius(progress.toFloat())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })

        navigation = view.findViewById(R.id.navigation_bar_accent)
        navigation.setOnCheckedChangeListener { _, isChecked ->
            changeNavigationIconColor(isChecked)
        }
        statusBar = view.findViewById(R.id.status_bar_accent)
        statusBar.setOnCheckedChangeListener { _, isChecked ->
            changeStatusBarIconColor(isChecked)
        }

        colorSeekBar = view.findViewById(R.id.colorSlider)
        colorSeekBar.setOnColorChangeListener { _, _, color ->
            if (!colorSeekBar.isFirstDraw) {
                changeBackgroundColor(color)
            }
        }

        return view
    }

    override fun configureBottomDrawer(): BottomDrawerDialog {
        return BottomDrawerDialog.build(context!!) {
            theme = R.style.Plain
            handleView = PlainHandleView(context).apply {
                val widthHandle =
                    resources.getDimensionPixelSize(R.dimen.bottom_sheet_handle_width)
                val heightHandle =
                    resources.getDimensionPixelSize(R.dimen.bottom_sheet_handle_height)
                val params =
                    FrameLayout.LayoutParams(widthHandle, heightHandle, Gravity.CENTER_HORIZONTAL)

                params.topMargin =
                    resources.getDimensionPixelSize(R.dimen.bottom_sheet_handle_top_margin)

                layoutParams = params
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putFloat("alphaCancelButton", alphaCancelButton)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        alphaCancelButton = savedInstanceState?.getFloat("alphaCancelButton") ?: 0f
        cancelButton.alpha = alphaCancelButton
        cancelButton.isEnabled = alphaCancelButton > 0
    }
}

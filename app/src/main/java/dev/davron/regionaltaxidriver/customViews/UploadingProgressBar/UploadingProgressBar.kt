package dev.davron.regionaltaxidriver.customViews.UploadingProgressBar


import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import dev.davron.regionaltaxidriver.R
import dev.davron.regionaltaxidriver.databinding.FileUploadProgressBarBinding

class UploadingProgressBar(
    val context: Context,
    val view: View,
    val onCancelButtonClickListener: OnCancelButtonClickListener
) {
    val binding = FileUploadProgressBarBinding.bind(view)

    init {
        binding.root.visibility = View.VISIBLE
        val animation = AnimationUtils.loadAnimation(context, R.anim.rotate_anim)
        animation.fillAfter = true
        animation.setInterpolator(LinearInterpolator())
        animation.setRepeatCount(Animation.INFINITE)
        binding.progressBar.startAnimation(animation)

        setUpProgressBar()
        setLoadingDone()

        binding.root.setOnClickListener {
            countDownTimer.removeCallbacks(runnable)
            onCancelButtonClickListener.onCancelClicked()
        }
    }

    private lateinit var countDownTimer: Handler
    private var progress = 0

    private var runnable: Runnable = object : Runnable {
        override fun run() {
            progress += 10
            binding.progressBar.progress = progress

            if (2000 != progress && progress <= 2500) {
                countDownTimer.postDelayed(this, 10)
            }

            if (progress >= 2500) {
                binding.root.visibility = View.GONE
                onCancelButtonClickListener.onUploadingFinished()
            }
        }
    }

    fun setLoadingDone() {
        countDownTimer.postDelayed(runnable, 10)
    }

    private fun setUpProgressBar() {
        binding.progressBar.max = 2500
        countDownTimer = Handler(Looper.getMainLooper())
        countDownTimer.postDelayed(runnable, 10)
    }

    fun realeseProgressBar() {
        if (this::countDownTimer.isInitialized) {
            countDownTimer.removeCallbacks(runnable)
            binding.root.visibility = View.GONE
        }
    }


    interface OnCancelButtonClickListener {
        fun onUploadingFinished()
        fun onCancelClicked()
    }
}
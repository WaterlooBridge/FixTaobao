package com.zhenl.fixtaobao.base

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.zhenl.fixtaobao.MyApplication
import com.zhenl.fixtaobao.R
import com.zhenl.fixtaobao.events.EventHandler
import com.zhenl.fixtaobao.widget.CircleImageView
import com.zhenl.fixtaobao.widget.CircularProgressDrawable

/**
 * Created by lin on 20-5-16.
 */
abstract class BaseActivity : AppCompatActivity(), EventHandler {

    fun <T : BaseViewModel> getViewModel(modelClass: Class<T>): T {
        val viewModel = ViewModelProvider(this).get(modelClass)
        viewModel.viewEvents.observe(this, Observer {
            onEventDispatched(it)
        })
        return viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyApplication.globalLoading.observe(this, Observer {
            if (it) showLoading() else hideLoading()
        })
    }

    private lateinit var mProgress: CircularProgressDrawable

    private val loadingDialog: Dialog by lazy {
        Dialog(this, R.style.loadingDialogStyle).also {
            val circleImageView = CircleImageView(this, Color.WHITE)
            mProgress = CircularProgressDrawable(this)
            mProgress.setStyle(CircularProgressDrawable.LARGE);
            circleImageView.setImageDrawable(mProgress)
            it.setContentView(circleImageView)
            it.setCanceledOnTouchOutside(false)
            val size = resources.getDimensionPixelSize(R.dimen.loading_dialog_size)
            it.window?.apply {
                attributes.width = size
                attributes.height = size
            }
        }
    }

    private fun showLoading() {
        loadingDialog.show()
        mProgress.start()
    }

    private fun hideLoading() {
        loadingDialog.dismiss()
        mProgress.stop()
    }
}
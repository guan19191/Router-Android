package com.kronos.router.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlin.properties.Delegates

class FragmentForResult : Fragment() {

    var onSuccess: () -> Unit = {}
    var onFail: () -> Unit = {}
    var clazz: Class<out Any>? = null

    private var code by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        code = arguments?.getInt("requestCode", 0) ?: 0
        val intent = Intent()
        clazz?.let { intent.setClass(requireContext(), it) }
        startActivityForResult(intent, code)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == code) {
            if (resultCode == Activity.RESULT_OK) {
                onSuccess.invoke()
            } else {
                onFail.invoke()
            }
        }
    }

    companion object {
        fun startActivityForResult(code: Int, clazz: Class<out Any>, activity: AppCompatActivity, bundle: Bundle? = null,
                                   onSuccess: () -> Unit = {},
                                   onFail: () -> Unit = {}) {
            val fragment = FragmentForResult()
            fragment.onSuccess = onSuccess
            fragment.onFail = onFail
            fragment.clazz = clazz
            val mBundle = Bundle()
            bundle?.apply {
                mBundle.putAll(this)
            }
            mBundle.putInt("requestCode", code)
            fragment.arguments = bundle
            activity.supportFragmentManager.beginTransaction().add(fragment, "FragmentForResult").commit()
        }
    }
}



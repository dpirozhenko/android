package com.example.lab3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.lab3.fragments.InputFragment
import com.example.lab3.fragments.ResultFragment

class MainActivity : AppCompatActivity(),
    InputFragment.OnInputConfirmedListener,
    ResultFragment.OnCancelListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, InputFragment())
            .commit()
    }

    override fun onInputConfirmed(shape: String, options: List<String>) {
        val resultFragment = ResultFragment.newInstance(shape, options)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, resultFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onCancel() {
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, InputFragment())
            .commit()
    }
}

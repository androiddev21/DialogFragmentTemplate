package com.example.dialogfragment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.dialogfragment.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var text = "default value"

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dialogFragmentListener: DialogFragmentListener = { requestKey, outputValue ->
            when (requestKey) {
                KEY_REQUEST_BY_BUTTON_CLICK -> this.text = outputValue
                //TODO add other
            }
        }

        DialogFragmentTemplate.setupListener(
            supportFragmentManager,
            this,
            KEY_REQUEST_BY_BUTTON_CLICK,
            dialogFragmentListener
        )

        binding.bTestDialog.setOnClickListener {
            DialogFragmentTemplate.show(
                supportFragmentManager,
                KEY_REQUEST_BY_BUTTON_CLICK,
                text
            )
        }
    }

    companion object {
        private const val KEY_REQUEST_BY_BUTTON_CLICK = "request_buy_button_click"
    }
}
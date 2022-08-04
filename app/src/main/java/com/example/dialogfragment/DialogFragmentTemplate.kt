package com.example.dialogfragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.example.dialogfragment.databinding.DialogFragmentCustomUiBinding

typealias DialogFragmentListener = (requestKey: String, outputValue: String) -> Unit

class DialogFragmentTemplate : DialogFragment() {

    private val requestKey: String
        get() = requireArguments().getString(KEY_REQUEST_KEY, "")

    private val inputValue: String
        get() = requireArguments().getString(KEY_INPUT_VALUE, "")

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogFragmentCustomUiBinding.inflate(layoutInflater)
        binding.bOk.setOnClickListener {
            parentFragmentManager.setFragmentResult(
                requestKey, bundleOf(KEY_OUTPUT_VALUE to "${binding.inputField.text}")
            )
            dismiss()
        }
        binding.inputField.setText(inputValue)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setOnCancelListener {
                Toast.makeText(requireContext(), "Dialog was cancelled", Toast.LENGTH_SHORT).show()
            }
            .setOnDismissListener {
                hideKeyboard(binding.inputField)
            }
            .create()
        dialog.setOnShowListener {
            showKeyboard(binding.inputField)
        }

        return dialog
    }

    private fun showKeyboard(view: View) {
        view.post {
            getInputMethodManager(view).showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun hideKeyboard(view: View) {
        getInputMethodManager(view).hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun getInputMethodManager(view: View): InputMethodManager {
        val context = view.context
        return context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    companion object {
        private val TAG = DialogFragmentTemplate::class.java.simpleName
        private const val KEY_REQUEST_KEY = "request_key"
        private const val KEY_INPUT_VALUE = "input_value"
        private const val KEY_OUTPUT_VALUE = "output_value"

        fun show(manager: FragmentManager, requestKey: String, inputValue: String) {
            val dialogFragment = DialogFragmentTemplate()
            dialogFragment.arguments = bundleOf(
                KEY_REQUEST_KEY to requestKey,
                KEY_INPUT_VALUE to inputValue
            )
            dialogFragment.show(manager, TAG)
        }

        fun setupListener(
            manager: FragmentManager,
            lifecycleOwner: LifecycleOwner,
            requestKey: String,
            listener: DialogFragmentListener
        ) {
            manager.setFragmentResultListener(requestKey, lifecycleOwner) { key, result ->
                listener.invoke(key, result.getString(KEY_OUTPUT_VALUE, ""))
            }
        }
    }
}
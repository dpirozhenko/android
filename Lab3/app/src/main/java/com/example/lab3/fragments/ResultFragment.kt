package com.example.lab3.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.lab3.R

class ResultFragment : Fragment() {

    interface OnCancelListener {
        fun onCancel()
    }

    private var listener: OnCancelListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnCancelListener) {
            listener = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_result, container, false)

        val resultText = view.findViewById<TextView>(R.id.resultText)
        val cancelButton = view.findViewById<Button>(R.id.buttonCancel)

        val shape = arguments?.getString("shape") ?: ""
        val options = arguments?.getStringArrayList("options") ?: arrayListOf()

        resultText.text = "Фігура: $shape\nОбрано: ${options.joinToString(", ")}"

        cancelButton.setOnClickListener {
            listener?.onCancel()
        }

        return view
    }

    companion object {
        fun newInstance(shape: String, options: List<String>): ResultFragment {
            val fragment = ResultFragment()
            val args = Bundle()
            args.putString("shape", shape)
            args.putStringArrayList("options", ArrayList(options))
            fragment.arguments = args
            return fragment
        }
    }
}

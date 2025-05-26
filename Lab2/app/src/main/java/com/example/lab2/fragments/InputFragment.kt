package com.example.lab2.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.lab2.R

class InputFragment : Fragment() {

    private var listener: OnInputConfirmedListener? = null

    interface OnInputConfirmedListener {
        fun onInputConfirmed(shape: String, options: List<String>)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnInputConfirmedListener) {
            listener = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_input, container, false)

        val shapeGroup = view.findViewById<RadioGroup>(R.id.shapeGroup)
        val checkArea = view.findViewById<CheckBox>(R.id.checkArea)
        val checkPerimeter = view.findViewById<CheckBox>(R.id.checkPerimeter)
        val buttonOk = view.findViewById<Button>(R.id.buttonOk)

        buttonOk.setOnClickListener {
            val selectedShapeId = shapeGroup.checkedRadioButtonId
            if (selectedShapeId == -1 || (!checkArea.isChecked && !checkPerimeter.isChecked)) {
                Toast.makeText(requireContext(), "Будь ласка, зробіть вибір!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val shape = view.findViewById<RadioButton>(selectedShapeId).text.toString()
            val options = mutableListOf<String>()
            if (checkArea.isChecked) options.add("площа")
            if (checkPerimeter.isChecked) options.add("периметр")

            listener?.onInputConfirmed(shape, options)
        }

        return view
    }
}

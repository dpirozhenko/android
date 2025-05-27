package com.example.lab3.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.lab3.R
import com.example.lab3.ResultViewActivity
import com.example.lab3.storage.ResultDatabaseHelper

class InputFragment : Fragment() {

    interface OnInputConfirmedListener {
        fun onInputConfirmed(shape: String, options: List<String>)
    }

    private var listener: OnInputConfirmedListener? = null

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
        val buttonOpen = view.findViewById<Button>(R.id.buttonOpen)

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
            val optionsText = options.joinToString(", ")

            val db = ResultDatabaseHelper(requireContext())
            val success = db.insertResult(shape, optionsText)
            if (success) {
                Toast.makeText(requireContext(), "Результат збережено", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Помилка запису", Toast.LENGTH_SHORT).show()
            }

            listener?.onInputConfirmed(shape, options)
        }

        buttonOpen.setOnClickListener {
            startActivity(Intent(requireContext(), com.example.lab3.ResultViewActivity::class.java))
        }

        return view
    }
}

package com.example.lab1

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val shapeGroup = findViewById<RadioGroup>(R.id.shapeGroup)
        val checkArea = findViewById<CheckBox>(R.id.checkArea)
        val checkPerimeter = findViewById<CheckBox>(R.id.checkPerimeter)
        val buttonOk = findViewById<Button>(R.id.buttonOk)
        val resultText = findViewById<TextView>(R.id.resultText)

        buttonOk.setOnClickListener {
            val selectedShapeId = shapeGroup.checkedRadioButtonId

            if (selectedShapeId == -1 || (!checkArea.isChecked && !checkPerimeter.isChecked)) {
                Toast.makeText(this, "Будь ласка, зробіть вибір!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val shapeName = findViewById<RadioButton>(selectedShapeId).text
            val selectedParams = mutableListOf<String>()
            if (checkArea.isChecked) selectedParams.add("площа")
            if (checkPerimeter.isChecked) selectedParams.add("периметр")

            val result = "Фігура: $shapeName\nОбрано: ${selectedParams.joinToString(", ")}"
            resultText.text = result
        }
    }
}

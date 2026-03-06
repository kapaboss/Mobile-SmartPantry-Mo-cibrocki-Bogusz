package com.example.mobile_smart_pantry_project_iv

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mobile_smart_pantry_project_iv.databinding.ActivityMainBinding
import data.Item
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    val itemList = mutableListOf<Item>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val categories = listOf("Food", "Tool", "Equipment")

        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.categorySpinner.adapter = categoryAdapter

        binding.addItemButton.setOnClickListener {
            val name = binding.itemNameEditText.text.toString()
            val category = binding.categorySpinner.selectedItem.toString()
            val quantity = binding.quantityNumberPicker.value.toInt()

            if(name.isNotBlank()&&category.isNotBlank()&&quantity!=0) {

                val newItem = Item(
                    (Random.nextInt(100, 1000)).toString(), name, quantity, category
                )
                itemList.add(newItem)

                Toast.makeText(
                    this,
                    "Dodano: $name",
                    Toast.LENGTH_SHORT).show()

                binding.itemNameEditText.text.clear()
                binding.categorySpinner.setSelection(0)
                binding.quantityNumberPicker.value=0
            } else {
                Toast.makeText(this, "Uzupełnij informacje", Toast.LENGTH_SHORT).show()
            }


        }
    }
}
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
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import kotlin.random.Random

class MainActivity : AppCompatActivity() {



    lateinit var binding: ActivityMainBinding

    val itemList = mutableListOf<Item>()

    val items = mutableListOf<String>()

    lateinit var listAdapter: ArrayAdapter<String>

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

        binding.quantitySeekBar.setOnSeekBarChangeListener(object :
            android.widget.SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: android.widget.SeekBar?, progress: Int, fromUser: Boolean) {
                binding.quantityText.text = "Ilość: $progress"
            }

            override fun onStartTrackingTouch(seekBar: android.widget.SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: android.widget.SeekBar?) {}
        })


        listAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            items
        )
        binding.listViewItems.adapter = listAdapter

        binding.addItemButton.setOnClickListener {
            val name = binding.itemNameEditText.text.toString()
            val category = binding.categorySpinner.selectedItem.toString()
            val quantity = binding.quantitySeekBar.progress

            if(name.isNotBlank()&&category.isNotBlank()&&quantity!=0) {

                val newItem = Item(
                    (Random.nextInt(100, 1000)).toString(), name, quantity, category
                )
                itemList.add(newItem)

                items.add(" ID:${newItem.UUID} - ${newItem.Name} - ${newItem.Quantity} - ${newItem.Category}")
                listAdapter.notifyDataSetChanged()

                Toast.makeText(
                    this,
                    "Dodano: $name",
                    Toast.LENGTH_SHORT).show()

                binding.itemNameEditText.text.clear()
                binding.categorySpinner.setSelection(0)
                binding.quantitySeekBar.progress = 0
                binding.quantityText.text = "Ilość: 0"
            } else {
                Toast.makeText(this, "Uzupełnij informacje", Toast.LENGTH_SHORT).show()
            }


        }

        binding.saveButton.setOnClickListener {
            saveItemsToJsonFile()
        }

        loadItemsFromJsonFile()

    }

    private fun saveItemsToJsonFile(){
        try {
            val json = Json {prettyPrint = true}
            val jsonString = json.encodeToString(itemList)

            val file = File(filesDir, "items.json")
            file.writeText(jsonString)

            Toast.makeText(
                this,
                "Zapisano ${itemList.size} przedmiotów do magazynu",
                Toast.LENGTH_SHORT
            ).show()
        }catch (e: Exception){

            Toast.makeText(
                this,
                "Błąd zapisu pliku!",
                Toast.LENGTH_SHORT
            ).show()

            e.printStackTrace()
        }
    }

    private fun loadItemsFromJsonFile() {

        try {
            val file = File(filesDir, "items.json")
            if (!file.exists()) return

            val jsonString = file.readText()
            val json = Json {ignoreUnknownKeys = true}
            val loadedList = json.decodeFromString<List<Item>>(jsonString)

            itemList.clear()
            itemList.addAll(loadedList)

            items.clear()
            items.addAll(
                itemList.map {
                    "ID:${it.UUID} ${it.Name} (${it.Category}) Ilość: ${it.Quantity}"
                }
            )
            listAdapter.notifyDataSetChanged()
        }catch (e: java.lang.Exception) {
            Toast.makeText(
                this,
                "Błąd odczytu pliku!",
                Toast.LENGTH_SHORT
            ).show()
            e.printStackTrace()
        }
    }


}
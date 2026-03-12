package com.example.mobile_smart_pantry_project_iv

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mobile_smart_pantry_project_iv.databinding.ActivityEditItemBinding
import data.Item

class EditItemActivity : AppCompatActivity() {

    lateinit var binding: ActivityEditItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEditItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.quantityUpdateSeekBar.setOnSeekBarChangeListener(object :
            android.widget.SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: android.widget.SeekBar?, progress: Int, fromUser: Boolean) {
                binding.quantityUpdateText.text = "Ilość: $progress"
            }

            override fun onStartTrackingTouch(seekBar: android.widget.SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: android.widget.SeekBar?) {}
        })

        val categories = resources.getStringArray(R.array.item_categories)

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            categories
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categoryUpdateSpinner.adapter = adapter

        val itemToEdit = if (Build.VERSION.SDK_INT >= 33) {
            intent.getSerializableExtra("EXTRA_ITEM", Item::class.java)
        }else{
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("EXTRA_ITEM") as? Item
        }

        if(itemToEdit == null){
            finish()
            return
        }

        binding.itemNameUpdateEditText.setText(itemToEdit.Name)
        binding.quantityUpdateSeekBar.progress = (itemToEdit.Quantity)
        val currentCategoryIndex = adapter.getPosition(itemToEdit.Category)
        binding.categoryUpdateSpinner.setSelection(currentCategoryIndex)

        binding.saveChangesButton.setOnClickListener {

            val newName = binding.itemNameUpdateEditText.text.toString()
            val newCategory = binding.categoryUpdateSpinner.selectedItem.toString()
            val newQuantity = binding.quantityUpdateSeekBar.progress

            if (newName.isNotEmpty()){
                val updateItem = Item(itemToEdit.UUID, newName, newQuantity, newCategory)
                val returnIntent = Intent()
                returnIntent.putExtra("UPDATED_ITEM", updateItem)

                val originalPosition = intent.getIntExtra("EXTRA_POSITION", -1)
                returnIntent.putExtra("POSITION", originalPosition)

                setResult(RESULT_OK, returnIntent)

                finish()
            }else{
                Toast.makeText(
                    this,
                    "Nazwa jest wymagana",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
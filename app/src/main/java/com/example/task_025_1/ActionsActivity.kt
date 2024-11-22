package com.example.task_025_1

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import kotlin.system.exitProcess

class ActionsActivity : AppCompatActivity() {

    private var productId = 0
    val dataBase = DBHelper(this)

    private lateinit var toolbarMain: Toolbar
    private lateinit var updateBTN: Button
    private lateinit var deleteBTN: Button
    private lateinit var backBTN:   Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actions)

        init()

        setSupportActionBar(toolbarMain)
        title = "Действия с выбранным товаром."

        productId = intent?.getStringExtra("productId")!!.toInt()

        backBTN.setOnClickListener{
            toMainDisplay()
        }
    }

    private fun init() {
        toolbarMain = findViewById(R.id.toolbarMain)

        updateBTN = findViewById(R.id.updateBTN)
        deleteBTN = findViewById(R.id.deleteBTN)
        backBTN = findViewById(R.id.backBTN)
    }

    private fun toMainDisplay() {
        val intent = Intent(this@ActionsActivity, MainActivity::class.java);
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        updateBTN.setOnClickListener{
            updateRecord()
        }
        deleteBTN.setOnClickListener{
            deleteRecord()
        }
    }

    @SuppressLint("MissingInflatedId")
    private fun updateRecord() {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.update_dialog, null)
        dialogBuilder.setView(dialogView)
        val editName = dialogView.findViewById<EditText>(R.id.updateNameET)
        val editWeight = dialogView.findViewById<EditText>(R.id.updateWeightET)
        val editPrice = dialogView.findViewById<EditText>(R.id.updatePriceET)

        dialogBuilder.setTitle("Обновить запись")
        dialogBuilder.setMessage("введите данные ниже:")
        dialogBuilder.setPositiveButton("Обновить"){_, _ ->
            val updateName = editName.text.toString()
            val updateWeight = editWeight.text.toString()
            val updatePrice = editPrice.text.toString()
            if ( updateName.trim() != "" &&
                updateWeight.trim() != "" && updatePrice.trim() != "") {
                val updatetdProduct = Product(
                    productId, updateName.toString(), updateWeight.toDouble(), updatePrice.toInt()
                )
                dataBase.updateProduct(updatetdProduct)
                Toast.makeText(applicationContext, "Продукт изменен", Toast.LENGTH_LONG).show()
                toMainDisplay()
            }
        }
        dialogBuilder.setNegativeButton("Отмена"){dialog, which ->}
        dialogBuilder.create().show()
    }

    @SuppressLint("MissingInflatedId")
    private fun deleteRecord() {
        val product = Product(productId, "", 0.0, 0)
        dataBase.daleteProduct(product)
        Toast.makeText(applicationContext, "Продукт удален", Toast.LENGTH_LONG).show()
        toMainDisplay()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return  true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.exitMenuMain->{
                moveTaskToBack(true);
                exitProcess(-1)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
package com.example.task_025_1

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    private var intent: Intent? = null
    var products: MutableList<Product> = mutableListOf()
    var listAdapter: ProductListAdapter? = null
    val dataBase = DBHelper(this)

    private lateinit var toolbarMain: Toolbar
    private lateinit var productIdET: EditText
    private lateinit var productNameET: EditText
    private lateinit var productWeightET: EditText
    private lateinit var productPriceET: EditText
    private lateinit var listViewLV: ListView

    private lateinit var saveBTN: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()

        intent = Intent(this, ActionsActivity::class.java);

        setSupportActionBar(toolbarMain)
        title = "Потребительская корзина."

        saveBTN.setOnClickListener{
            saveRecord()
        }

        listViewLV.onItemClickListener = AdapterView.OnItemClickListener {parent, v, position, id ->
            intent?.putExtra("productId", products[position].productId.toString())
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        viewDataAdapter()
    }

    private fun init() {
        listViewLV = findViewById(R.id.listViewLV)
        toolbarMain = findViewById(R.id.toolbarMain)
        productIdET = findViewById(R.id.productIdET)
        productNameET = findViewById(R.id.productNameET)
        productWeightET = findViewById(R.id.productWeightET)
        productPriceET = findViewById(R.id.productPriceET)

        saveBTN = findViewById(R.id.saveBTN)
        viewDataAdapter()
    }

    private fun saveRecord() {
        val id = productIdET.text.toString()
        val name = productNameET.text.toString()
        val weight = productWeightET.text.toString()
        val price = productPriceET.text.toString()
        if (id.trim() != "" && name.trim() != "" && weight.trim() != "" && price.trim()!= "") {
            productIdET.setText("")
            productNameET.setText("")
            productWeightET.setText("")
            productPriceET.setText("")
            val product = Product(Integer.parseInt(id), name, weight.toDouble(), price.toInt())
            dataBase.addProduct(product)
            Toast.makeText(applicationContext, "Запись добавлена", Toast.LENGTH_LONG).show()
            viewDataAdapter()
        }
    }

    private fun viewDataAdapter() {
        products = dataBase.readProducts()
        listAdapter = ProductListAdapter(this, products)
        listViewLV.adapter = listAdapter
        listAdapter?.notifyDataSetChanged()
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
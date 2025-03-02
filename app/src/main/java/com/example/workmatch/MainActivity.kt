package com.example.workmatch
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var vagaAdapter: VagaAdapter
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DBHelper(this)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val vagasList = dbHelper.getAllVagas()

        vagaAdapter = VagaAdapter(vagasList) { vaga ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("vaga_id", vaga.id)
            startActivity(intent)
        }

        recyclerView.adapter = vagaAdapter

        val btnCriar: Button = findViewById(R.id.btnCriar)
        btnCriar.setOnClickListener {
            val intent = Intent(this, CreateActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}

package com.example.workmatch
import android.content.Intent
import android.media.MediaPlayer
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

        // Configura o RecyclerView para listar as vagas
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Recupera a lista de vagas do banco de dados
        val vagasList = dbHelper.getAllVagas()

        // Configura o adaptador para o RecyclerView com a lista de vagas
        vagaAdapter = VagaAdapter(vagasList) { vaga ->
            val mediaPlayer = MediaPlayer.create(this, R.raw.notification)
            mediaPlayer.start()

            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("vaga_id", vaga.id)
            startActivity(intent)
        }

        recyclerView.adapter = vagaAdapter

        // Botão de criar nova vaga
        val btnCriar: Button = findViewById(R.id.btnCriar)
        btnCriar.setOnClickListener {
            var mediaPlayer = MediaPlayer.create(this, R.raw.init)
            mediaPlayer.start()

            val intent = Intent(this, CreateActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}

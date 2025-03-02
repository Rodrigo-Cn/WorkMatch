package com.example.workmatch

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.workmatch.databinding.ActivityCreateBinding

class CreateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.btnCadastrarVaga.setOnClickListener {
            cadastrarVaga()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                voltarParaMain()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun voltarParaMain() {
        var mediaPlayer = MediaPlayer.create(this, R.raw.init)
        mediaPlayer.start()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun cadastrarVaga() {
        val nomeVaga = binding.edtNomeVaga.text.toString()
        val setor = binding.edtSetor.text.toString()
        val salario = binding.edtSalario.text.toString().toDoubleOrNull() ?: 0.0
        val modeloTrabalho = binding.edtModeloTrabalho.text.toString()
        val jornadaTrabalho = binding.edtJornadaTrabalho.text.toString()
        val nomeEmpresa = binding.edtNomeEmpresa.text.toString()

        if (nomeVaga.isNotEmpty() && setor.isNotEmpty()) {
            val vaga = Vaga(nome = nomeVaga, setor = setor, salario = salario, modeloTrabalho = modeloTrabalho, jornadaTrabalho = jornadaTrabalho, nomeEmpresa = nomeEmpresa)

            val dbHelper = DBHelper(this)
            dbHelper.addVaga(vaga)

            val mediaPlayer = MediaPlayer.create(this, R.raw.notification)
            mediaPlayer.start()
            Toast.makeText(this, "Vaga cadastrada com sucesso!", Toast.LENGTH_SHORT).show()

            voltarParaMain()
        } else {
            val mediaPlayer = MediaPlayer.create(this, R.raw.notification)
            mediaPlayer.start()
            Toast.makeText(this, "Preencha todos os campos obrigatórios!", Toast.LENGTH_SHORT).show()
        }
    }
}

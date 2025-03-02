package com.example.workmatch

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.workmatch.databinding.ActivityEditBinding

class EditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditBinding
    private var vagaId: Long = -1 // ID da vaga que será editada

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Recupera o ID da vaga da Intent
        vagaId = intent.getLongExtra("vaga_id", -1)

        if (vagaId != -1L) {
            // Busca a vaga pelo ID
            val dbHelper = DBHelper(this)
            val vaga = dbHelper.getVagaById(vagaId)

            // Preenche os campos com os dados da vaga
            if (vaga != null) {
                binding.edtNomeVaga.setText(vaga.nome)
                binding.edtSetor.setText(vaga.setor)
                binding.edtSalario.setText(vaga.salario.toString())
                binding.edtModeloTrabalho.setText(vaga.modeloTrabalho)
                binding.edtJornadaTrabalho.setText(vaga.jornadaTrabalho)
                binding.edtNomeEmpresa.setText(vaga.nomeEmpresa)
            }
        }

        binding.btnEditarVaga.setOnClickListener {
            editarVaga()
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

    private fun editarVaga() {
        val nomeVaga = binding.edtNomeVaga.text.toString()
        val setor = binding.edtSetor.text.toString()
        val salario = binding.edtSalario.text.toString().toDoubleOrNull() ?: 0.0
        val modeloTrabalho = binding.edtModeloTrabalho.text.toString()
        val jornadaTrabalho = binding.edtJornadaTrabalho.text.toString()
        val nomeEmpresa = binding.edtNomeEmpresa.text.toString()

        if (nomeVaga.isNotEmpty() && setor.isNotEmpty()) {
            val vagaAtualizada = Vaga(
                id = vagaId,
                nome = nomeVaga,
                setor = setor,
                salario = salario,
                modeloTrabalho = modeloTrabalho,
                jornadaTrabalho = jornadaTrabalho,
                nomeEmpresa = nomeEmpresa
            )

            val dbHelper = DBHelper(this)
            dbHelper.updateVaga(vagaAtualizada)

            val mediaPlayer = MediaPlayer.create(this, R.raw.notification)
            mediaPlayer.start()
            Toast.makeText(this, "Vaga editada com sucesso!", Toast.LENGTH_SHORT).show()
            voltarParaMain()
        } else {
            val mediaPlayer = MediaPlayer.create(this, R.raw.notification)
            mediaPlayer.start()
            Toast.makeText(this, "Preencha todos os campos obrigatórios!", Toast.LENGTH_SHORT).show()
        }
    }
}

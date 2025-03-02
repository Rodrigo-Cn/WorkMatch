package com.example.workmatch

import android.content.DialogInterface
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.workmatch.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private var vagaId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        vagaId = intent.getLongExtra("vaga_id", -1)

        if (vagaId != -1L) {
            carregarDetalhesVaga(vagaId)
        } else {
            Toast.makeText(this, "Erro ao carregar detalhes da vaga", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Configurar clique dos botões
        binding.btnEditarVaga.setOnClickListener { editarVaga() }
        binding.btnDeletarVaga.setOnClickListener { confirmarExclusaoVaga() }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun carregarDetalhesVaga(vagaId: Long) {
        val dbHelper = DBHelper(this)
        val vaga = dbHelper.getVagaById(vagaId)

        vaga?.let {
            binding.tvNomeVaga.text = it.nome
            binding.tvSetor.text = it.setor
            binding.tvSalario.text = "R$ ${it.salario}"
            binding.tvModeloTrabalho.text = it.modeloTrabalho
            binding.tvJornadaTrabalho.text = it.jornadaTrabalho
            binding.tvNomeEmpresa.text = it.nomeEmpresa
        } ?: run {
            Toast.makeText(this, "Vaga não encontrada!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    // Ir para a tela de edição
    private fun editarVaga() {
        val intent = Intent(this, EditActivity::class.java)
        intent.putExtra("vaga_id", vagaId)
        startActivity(intent)
    }

    // Exibir diálogo de confirmação antes de excluir
    private fun confirmarExclusaoVaga() {
        AlertDialog.Builder(this)
            .setTitle("Confirmar Exclusão")
            .setMessage("Tem certeza que deseja excluir esta vaga?")
            .setPositiveButton("Sim") { _: DialogInterface, _: Int ->
                deletarVaga()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    // Deletar a vaga do banco e finalizar a Activity
    private fun deletarVaga() {
        val dbHelper = DBHelper(this)
        val sucesso = dbHelper.deleteVaga(vagaId)

        if (sucesso) {
            Toast.makeText(this, "Vaga excluída com sucesso!", Toast.LENGTH_SHORT).show()
            val mediaPlayer = MediaPlayer.create(this, R.raw.notification)
            mediaPlayer.start()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Fecha a tela após exclusão
        } else {
            val mediaPlayer = MediaPlayer.create(this, R.raw.notification)
            mediaPlayer.start()
            Toast.makeText(this, "Erro ao excluir a vaga.", Toast.LENGTH_SHORT).show()
        }
    }
}

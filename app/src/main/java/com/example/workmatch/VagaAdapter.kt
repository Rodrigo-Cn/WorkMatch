package com.example.workmatch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class VagaAdapter(
    private val vagas: List<Vaga>,
    private val onDetalhesClickListener: (Vaga) -> Unit 
) : RecyclerView.Adapter<VagaAdapter.VagaViewHolder>() {

    // ViewHolder que contém as referências para os elementos do layout
    class VagaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nome: TextView = itemView.findViewById(R.id.tvNome)
        val detalhesButton: Button = itemView.findViewById(R.id.btnDetalhes)
    }

    // Cria o ViewHolder para cada item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VagaViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_vaga, parent, false)
        return VagaViewHolder(itemView)
    }

    // Configura o ViewHolder com os dados da vaga
    override fun onBindViewHolder(holder: VagaViewHolder, position: Int) {
        val vaga = vagas[position]
        holder.nome.text = vaga.nome // Preenche o nome da vaga

        // Configura o clique no botão de detalhes
        holder.detalhesButton.setOnClickListener {
            onDetalhesClickListener(vaga)
        }
    }

    // Retorna o número total de itens
    override fun getItemCount(): Int = vagas.size
}

package com.example.workmatch

data class Vaga(
    val id: Long = 0,
    val nome: String,
    val setor: String,
    val salario: Double,
    val modeloTrabalho: String,
    val jornadaTrabalho: String,
    val nomeEmpresa: String
)

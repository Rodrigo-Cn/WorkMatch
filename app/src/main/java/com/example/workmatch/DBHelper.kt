package com.example.workmatch

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, "VagasDB", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = """
            CREATE TABLE Vagas (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nome TEXT,
                setor TEXT,
                salario REAL,
                modeloTrabalho TEXT,
                jornadaTrabalho TEXT,
                nomeEmpresa TEXT
            )
        """
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS Vagas")
        onCreate(db)
    }

    // Create - Adiciona uma nova vaga
    fun addVaga(vaga: Vaga) {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put("nome", vaga.nome)
            put("setor", vaga.setor)
            put("salario", vaga.salario)
            put("modeloTrabalho", vaga.modeloTrabalho)
            put("jornadaTrabalho", vaga.jornadaTrabalho)
            put("nomeEmpresa", vaga.nomeEmpresa)
        }
        db.insert("Vagas", null, contentValues)
        db.close()
    }

    // Read - Retorna todas as vagas
    fun getAllVagas(): List<Vaga> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Vagas", null)
        val vagasList = mutableListOf<Vaga>()

        if (cursor.moveToFirst()) {
            do {
                val idColumnIndex = cursor.getColumnIndex("id")
                val nomeColumnIndex = cursor.getColumnIndex("nome")
                val setorColumnIndex = cursor.getColumnIndex("setor")
                val salarioColumnIndex = cursor.getColumnIndex("salario")
                val modeloTrabalhoColumnIndex = cursor.getColumnIndex("modeloTrabalho")
                val jornadaTrabalhoColumnIndex = cursor.getColumnIndex("jornadaTrabalho")
                val nomeEmpresaColumnIndex = cursor.getColumnIndex("nomeEmpresa")

                val vaga = Vaga(
                    id = if (idColumnIndex >= 0) cursor.getLong(idColumnIndex) else -1,
                    nome = if (nomeColumnIndex >= 0) cursor.getString(nomeColumnIndex) else "",
                    setor = if (setorColumnIndex >= 0) cursor.getString(setorColumnIndex) else "",
                    salario = if (salarioColumnIndex >= 0) cursor.getDouble(salarioColumnIndex) else 0.0,
                    modeloTrabalho = if (modeloTrabalhoColumnIndex >= 0) cursor.getString(modeloTrabalhoColumnIndex) else "",
                    jornadaTrabalho = if (jornadaTrabalhoColumnIndex >= 0) cursor.getString(jornadaTrabalhoColumnIndex) else "",
                    nomeEmpresa = if (nomeEmpresaColumnIndex >= 0) cursor.getString(nomeEmpresaColumnIndex) else ""
                )
                vagasList.add(vaga)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return vagasList
    }

    // Update - Atualiza uma vaga
    fun updateVaga(vaga: Vaga) {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put("nome", vaga.nome)
            put("setor", vaga.setor)
            put("salario", vaga.salario)
            put("modeloTrabalho", vaga.modeloTrabalho)
            put("jornadaTrabalho", vaga.jornadaTrabalho)
            put("nomeEmpresa", vaga.nomeEmpresa)
        }

        val whereClause = "id = ?"
        val whereArgs = arrayOf(vaga.id.toString())

        db.update("Vagas", contentValues, whereClause, whereArgs)
        db.close()
    }

    // Delete - Deleta uma vaga pelo ID
    fun deleteVaga(vagaId: Long) {
        val db = writableDatabase
        val whereClause = "id = ?"
        val whereArgs = arrayOf(vagaId.toString())
        db.delete("Vagas", whereClause, whereArgs)
        db.close()
    }
}

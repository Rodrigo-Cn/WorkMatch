package com.example.workmatch

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, "VagasDB", null, 1) {
    // Cria a tabela de vagas
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

    // Deleta a tabela de vagas
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS Vagas")
        onCreate(db)
    }

    // Adiciona uma nova vaga
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

    // Retorna todas as vagas
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

    // Atualiza uma vaga
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

    // Deleta uma vaga pelo ID
    fun deleteVaga(vagaId: Long): Boolean {
        val db = writableDatabase
        val whereClause = "id = ?"
        val whereArgs = arrayOf(vagaId.toString())
        val rowsDeleted = db.delete("Vagas", whereClause, whereArgs)
        db.close()
        return rowsDeleted > 0
    }

    // Read - Retorna uma vaga pelo ID
    fun getVagaById(vagaId: Long): Vaga? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Vagas WHERE id = ?", arrayOf(vagaId.toString()))

        return if (cursor.moveToFirst()) {
            val vaga = Vaga(
                id = cursor.getLong(cursor.getColumnIndexOrThrow("id")),
                nome = cursor.getString(cursor.getColumnIndexOrThrow("nome")),
                setor = cursor.getString(cursor.getColumnIndexOrThrow("setor")),
                salario = cursor.getDouble(cursor.getColumnIndexOrThrow("salario")),
                modeloTrabalho = cursor.getString(cursor.getColumnIndexOrThrow("modeloTrabalho")),
                jornadaTrabalho = cursor.getString(cursor.getColumnIndexOrThrow("jornadaTrabalho")),
                nomeEmpresa = cursor.getString(cursor.getColumnIndexOrThrow("nomeEmpresa"))
            )
            cursor.close()
            vaga
        } else {
            cursor.close()
            null
        }
    }

}

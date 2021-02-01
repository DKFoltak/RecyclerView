package edu.gustavo.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ModeloAdapter(var modelos: ModeloViewModel) : RecyclerView.Adapter<ModeloAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var clase = view.findViewById<View>(R.id.textView_valClase) as TextView
        var materia = view.findViewById<View>(R.id.textView_valMateria) as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_clase, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val modelo = modelos.lista[position]
        holder.clase.text = modelo.clase
        holder.materia.text = modelo.materia
    }

    override fun getItemCount(): Int {
        return modelos.lista.size
    }
}

package edu.gustavo.recyclerview

import androidx.lifecycle.ViewModel

class ModeloViewModel : ViewModel() {
    var lista = MutableList<Modelo>(10, { i ->
        when (i) {
            0 -> Modelo("Android", "SO")
            1 -> Modelo("Kotlin", "Lenguaje Programacion")
            2 -> Modelo("Java", "Lenguaje Programacion")
            3 -> Modelo(".Net", "Framework")
            4 -> Modelo("C#", "Lenguaje Programacion")
            5 -> Modelo("JS", "Lenguaje Programacion")
            6 -> Modelo("ASP.Net", "Framework")
            7 -> Modelo("Windows", "SO")
            8 -> Modelo("Spring", "Framework")
            9 -> Modelo("DevOps", "Arquitectura")
            10 -> Modelo("IA", "Arquitectura")
            else -> Modelo("Internet", "Informatica")
        }
    })
}
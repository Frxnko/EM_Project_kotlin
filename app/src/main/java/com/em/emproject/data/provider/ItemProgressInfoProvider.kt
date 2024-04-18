package com.em.emproject.data.provider

import com.example.emcontrol.domain.model.ItemProgressInfo

class ItemProgressInfoProvider {
    companion object{
        val itemProjectTowerList = listOf<ItemProgressInfo>(ItemProgressInfo("1","TRABAJOS PRELIMINARES",0.08,0.00,1,"T","1"),
            ItemProgressInfo("1.1","Entrega de terreno, trazo, replanteo, niveles ",0.03,0.00,2,"P","1"),
            ItemProgressInfo("1.2","Transporte de estructuras metálicas",0.02,0.00,3,"P","1"),
            ItemProgressInfo("1.3","Limpieza, desbroce, cerco, depósito agua",0.03,0.00,4,"P","1"),
            ItemProgressInfo("2","CIMENTACION DE TORRE",0.29,0.00,5,"T","2"),
            ItemProgressInfo("2.1","Excavación de cimentación y mejoramiento de terreno.",0.05,0.00,6,"P","2"),
            ItemProgressInfo("2.2","Vaciado de solado de torre",0.01,0.00,7,"P","2"),
            ItemProgressInfo("2.3","Habilitación y armado de acero para zapatas y pedestales",0.07,0.00,8,"P","2"),
            ItemProgressInfo("2.4","Encofrado de pedestales colocación de plantilla",0.04,0.00,9,"P","2"),
            ItemProgressInfo("2.5","Vaciado de concreto en zapata y pedestales",0.06,0.00,10,"P","2"),
            ItemProgressInfo("2.6","Desencofrado y curado del concreto",0.01,0.00,11,"P","2"),
            ItemProgressInfo("2.7","Relleno y compactado del terreno",0.05,0.00,12,"P","2"),
            ItemProgressInfo("3","MONTAJE DE TORRE E INSTALACIONES",0.13,0.00,13,"T","3"),
            ItemProgressInfo("3.1","Clasificación de piezas (conteo y verificación)",0.01,0.00,14,"P","3"),
            ItemProgressInfo("3.2","Montaje de torre",0.09,0.00,15,"P","3"),
            ItemProgressInfo("3.3","Instalación de pararrayo, balizaje y aterramiento de torre",0.01,0.00,16,"P","3"),
            ItemProgressInfo("3.4","Pintura de torre",0.02,0.00,17,"P","3"),
            ItemProgressInfo("4","LOSA DE EQUIPOS",0.08,0.00,18,"T","4"),
            ItemProgressInfo("4.1","Excavación para losa",0.01,0.00,19,"P","4"),
            ItemProgressInfo("4.2","Vaciado de solado",0.01,0.00,20,"P","4"),
            ItemProgressInfo("4.3","Encofrado y colocación de armadura de acero",0.02,0.00,21,"P","4"),
            ItemProgressInfo("4.4","Vaciado de concreto en losa de equipos",0.03,0.00,22,"P","4"),
            ItemProgressInfo("4.5","Desencofrado y curado de concreto en losa",0.01,0.00,23,"P","4"),
            ItemProgressInfo("5","CERCO PERIMETRICO",0.23,0.00,24,"T","5"),
            ItemProgressInfo("5.1","Excavación de zanja ",0.01,0.00,25,"P","5"),
            ItemProgressInfo("5.2","Acero, Vaciado de cimiento corrido",0.04,0.00,26,"P","5"),
            ItemProgressInfo("5.3","Acero, encofrado de sobrecimiento",0.04,0.00,27,"P","5"),
            ItemProgressInfo("5.4","Vaciado de sobrecimiento",0.03,0.00,28,"P","5"),
            ItemProgressInfo("5.5","Asentado de ladrillo",0.07,0.00,29,"P","5"),
            ItemProgressInfo("5.6","Instalación portón",0.01,0.00,30,"P","5"),
            ItemProgressInfo("5.7","Instalación de concertina",0.01,0.00,31,"P","5"),
            ItemProgressInfo("5.8","Losa de ingreso",0.02,0.00,32,"P","5"),
            ItemProgressInfo("6","INSTALACIONES ELECTRICAS Y POZOS A TIERRA",0.12,0.00,33,"T","6"),
            ItemProgressInfo("6.1","Excavación de pozos",0.02,0.00,34,"P","6"),
            ItemProgressInfo("6.2","Tratamiento de pozos",0.02,0.00,35,"P","6"),
            ItemProgressInfo("6.3","Interconexión de pozos, entubado, cableado y soldadura",0.02,0.00,36,"P","6"),
            ItemProgressInfo("6.4","Aterramientos",0.02,0.00,37,"P","6"),
            ItemProgressInfo("6.5","Acometida y borneras",0.02,0.00,38,"P","6"),
            ItemProgressInfo("6.6","Instalación de tableros y luminarias ",0.02,0.00,39,"P","6"),
            ItemProgressInfo("7","ACABADOS",0.07,0.00,40,"T","7"),
            ItemProgressInfo("7.1","Tarrajeo de pedestales, losa de equipo y otros elementos",0.02,0.00,41,"P","7"),
            ItemProgressInfo("7.2","Colector de aguas pluviales",0.01,0.00,42,"P","7"),
            ItemProgressInfo("7.3","Enripiado",0.01,0.00,43,"P","7"),
            ItemProgressInfo("7.4","Pintado",0.02,0.00,44,"P","7"),
            ItemProgressInfo("7.5","Limpieza final de obra",0.01,0.00,45,"P","7"),


            )
    }
}
package com.czq.schemerun

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import jscheme.InputPort
import jscheme.Scheme
import jscheme.SchemePrimitives
import java.io.ByteArrayInputStream
import java.util.*


class MainActivity : AppCompatActivity() {
    var history:MutableList<String> = LinkedList();
    var currentIndex=-1;
    companion object {

        fun parseString(inputText:String) : Any{
           return stringToPort(inputText).read()
        }
        fun loadString(inputText: String,s:Scheme){
            s.load(stringToPort(inputText))
        }
        fun stringToPort(inputText: String): InputPort {
            val inputStream = ByteArrayInputStream(inputText.toByteArray())
            return InputPort(inputStream)
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadSchemePrimitives()
    }
    var scheme=Scheme();
    fun onRun(view: View) {
        var inputField = findViewById<EditText>(R.id.input)
        var outputField = findViewById<TextView>(R.id.output)
        var title = findViewById<TextView>(R.id.title)
        var inputText=inputField.text.toString()
        try {
            var input= parseString(inputText)
            title.text = input.toString()
            var result = scheme.eval(input)
            outputField.text = result.toString()
        }
        catch (e:Exception){
            title.text=e.toString()
        }
        inputField.text.clear()
        currentIndex++
        history.add(currentIndex,inputText)
    }

    fun onPreviousInput(view: View) {
        if(currentIndex>0)
            currentIndex--
        updateInputFromHistory()
    }
    fun updateInputFromHistory(){
        var inputField = findViewById<EditText>(R.id.input)
        inputField.setText(history[currentIndex])
    }
    fun onNextInput(view: View) {
        if(currentIndex<history.size-1)
            currentIndex++
        updateInputFromHistory()


    }

    fun onLoad(view: View) {
        loadSchemePrimitives()
    }
    fun loadSchemePrimitives(){
        var title = findViewById<TextView>(R.id.title)
        try{
            loadString(SchemePrimitives.CODE,scheme)
        }
        catch (e: Exception){
            title.text=e.toString()
        }
    }
}

package com.example.tiptime

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.RadioGroup
import com.example.tiptime.databinding.ActivityMainBinding
import java.text.NumberFormat
import kotlin.math.round

/*
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}*/

// Substituindo o código padrão acima, pois este possibilitará a vinculação de visualização
class MainActivity : AppCompatActivity() {

    // Declara uma variável de nível superior na classe para o objeto de vinculação
    // lateinit - código inicializará a variável antes de usá-la
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        // Especifica a raiz da hierarquia de visualizações no app ao invés de transmitir o ID de recurso
        setContentView(binding.root)

        // Define listener de clique no botão Calculate e faz com que ele chame o método calculateTip()
        binding.calculateButton.setOnClickListener{ calculateTip() }

        // Define listener de clique na tecla enter e chama função que fecha o teclado
        binding.costOfServiceEditText.setOnKeyListener { view, keyCode, _ -> handleKeyEvent(view, keyCode) }
    }

    private fun calculateTip() {
        // Acessa o atributo de texto na EditText Cost of Service e atribui a variável stringInTextField. É possível acessar o elemento
        // de IU usando o objeto binding e fazendo referência ao elemento da IU com base no nome do ID de recurso em camelCase.
        // .text no final instrui o app a usar o resultado (um objeto EditText) e acessar a propriedade text dele
        // toDouble() precisa ser chamado em uma String. O atributo text de um EditText é do tipo Editable,
        // por isso é necessário converter um Editable em uma String chamando o método toString()
        // Chama toString() em binding.costOfService.text para convertê-lo em uma String
        val stringInTextField = binding.costOfServiceEditText.text.toString()

        // Converte o texto em número decimal. Chamando o método toDoubleOrNull() em stringInTextField e armazena na variável cost.
        val cost = stringInTextField.toDoubleOrNull()

        // If the cost is null or 0, then display 0 tip and exit this function early.
        // A instrução return significa sair do método sem executar o restante das instruções.
        if (cost == null || cost == 0.0) {
            displayTip(0.0)
            return
        }

        // Acessa o atributo checkedRadioButtonId do RadioGroup tipOptions e a porcentagem da gorjeta
        val tipPercentage = when (binding.tipOptions.checkedRadioButtonId) {
            R.id.option_twenty_percent -> 0.20
            R.id.option_eighteen_percent -> 0.18
            else -> 0.15
        }

        // Gorjeta = orcentagem da gorjeta * custo do serviço, var pois o valor poderá ser arredondado
        var tip = tipPercentage * cost

        // Instrução if que atribui o teto da gorjeta à variável tip se a expressão for verdadeira
        // ceil arredonda o valor de tip para cima
        if(binding.roundUpSwitch.isChecked){
            tip = kotlin.math.ceil(tip)
        }
        // Display the formatted tip value on screen
        displayTip(tip)

        // Formatador numérico que pode ser usado para formatar os números como moedas
        //NumberFormat.getCurrencyInstance()
    }
    private fun displayTip(tip : Double){
        // Encadeia chamada para o método format() com a tip e atribui o resultado a variável formattedTip
        val formattedTip = NumberFormat.getCurrencyInstance().format(tip)

        // Define o texto do tipResult. chama getString(R.string.tip_amount, formattedTip) e o
        // atribui ao atributo text do resultado da gorjeta da TextView
        binding.tipResult.text = getString(R.string.tip_amount, formattedTip)
    }

    // Função auxiliar para ocultar teclado ao clicar em enter.
    // oculta o teclado na tela se o parâmetro de entrada keyCode é igual a KeyEvent.KEYCODE_ENTER.
    // O InputMethodManager controla se um teclado de software é exibido ou não e permite que o
    // usuário escolha qual teclado de software será exibido. O método retornará "true" se o
    // evento de tecla tiver sido processado. Caso contrário, retornará "false".
    private fun handleKeyEvent(view: View, keyCode: Int): Boolean {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            // Hide the keyboard
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            return true
        }
        return false
    }
}
package com.danielarmenta.dicewarepasswordgenerator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.danielarmenta.dicewarepasswordgenerator.data.WordManager
import com.danielarmenta.dicewarepasswordgenerator.ui.theme.DicewarePasswordGeneratorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DicewarePasswordGeneratorTheme {
                App()
            }
        }
    }
}

fun entropyOf(count: Int): Double {
    return 12.92 * count
}

fun intToDices(i: Int): String {
    val nums = arrayListOf<Int>()
    var myI = i
    while (myI > 0) {
        val mod = myI % 6
        print(mod)
        nums.add(mod+1)
        myI -= mod
        myI /= 6
    }

    return nums.reversed().joinToString("")
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WordsComponent(words: Array<Int>, modifier: Modifier = Modifier) {
    FlowRow(
        modifier = modifier
            .wrapContentWidth()
            .heightIn(min = 300.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        words.forEach { i ->
            val word = WordManager.words[i]
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(intToDices(i),
                    fontSize = 12.sp,
                    color = Color(0xFF404040),
                    )
                Text("$word ",
                    fontSize = 30.sp,
                    )
            }
        }
    }
}

@Composable
fun App(modifier: Modifier = Modifier) {
    var wordIndexes by remember {
        mutableStateOf(arrayOf<Int>())
    }
    val entropy = entropyOf(wordIndexes.size)

    val clipboard = LocalClipboardManager.current;

    Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Text("Bits of entropy: $entropy")
            // Text("Time to guess: 2 trillion years")

            WordsComponent(wordIndexes)
            Button(onClick = {
                wordIndexes = wordIndexes.plus((0..<WordManager.words.size).random())
            }) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = null
                )
                Text(text = "Add word")
            }
            Button(onClick = {
                val s = (wordIndexes.size-1)
                wordIndexes = wordIndexes.slice(0..<s).toTypedArray()
            }) {
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = null
                )

                Text(text = "Remove word")
            }
            Button(onClick = {
                wordIndexes = arrayOf()
            }) {
                Icon(
                    Icons.Filled.Refresh,
                    contentDescription = null
                )

                Text(text = "Reset words")
            }
            Button(onClick = {
                clipboard.setText(
                    AnnotatedString(
                        wordIndexes.joinToString(" ") { w -> WordManager.words[w] }
                    )
                )
            }) {
                Icon(
                    painterResource(R.drawable.content_copy),
                    contentDescription = null
                )

                Text(text = "Copy")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DicewarePasswordGeneratorTheme {
        App()
    }
}
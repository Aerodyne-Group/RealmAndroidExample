package com.aerodyne.realmexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aerodyne.realmexample.model.Frog
import com.aerodyne.realmexample.model.Pond
import com.aerodyne.realmexample.ui.theme.RealmExampleTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RealmExampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting()
                }
            }
        }
    }
}

@Composable
fun Greeting() {
    val result = remember { mutableStateOf<String?>("") }
    val coroutineScope = rememberCoroutineScope()

//    LaunchedEffect("key1") {
//        coroutineScope.launch {
//            withContext(Dispatchers.IO) {
//                RealmHelper.getAllUser()
//                result.value += ""
//            }
//        }
//    }

    Column(modifier = Modifier.padding(15.dp)) {
        Row {
            Button(modifier = Modifier.wrapContentSize(), onClick = {
                coroutineScope.launch {
                    withContext(Dispatchers.IO) {
                        try {
                            val frog = Frog()
                            frog.name = "Frog1"
                            frog.age = "30"
                            val pond  = Pond()
                            pond.name = "Pond1"
                            frog.pond = pond
                            RealmHelper.saveFrog(frog)
                        } catch (t: Throwable) {
                            t.printStackTrace()
                        }
                    }
                }
            }) {
                Text(text = "Add Frog")
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(modifier = Modifier.wrapContentSize(), onClick = {
                coroutineScope.launch {
                    withContext(Dispatchers.IO) {
                        result.value = ""
                        RealmHelper.getAllFrog().forEach {
                            result.value += "Frog:${it.name} Age:${it.age} Pond:${it.pond?.name}\n"
                        }
                    }
                }
            }) {
                Text(text = "Load Frog")
            }
        }

        Text(
            text = "${result.value}",
            maxLines = 2,
            modifier = Modifier
                .wrapContentSize()
                .padding(10.dp, 10.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RealmExampleTheme {
        Greeting()
    }
}
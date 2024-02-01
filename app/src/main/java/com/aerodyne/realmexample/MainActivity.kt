package com.aerodyne.realmexample

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aerodyne.realmexample.model.Frog
import com.aerodyne.realmexample.model.Pond
import com.aerodyne.realmexample.ui.theme.RealmExampleTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
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
    val coroutineScope = rememberCoroutineScope()
    val frogs = MutableStateFlow(SnapshotStateList<Frog>())
    //val frogs: StateFlow<SnapshotStateList<Frog>> = _frogs

    LaunchedEffect("gans") {
        Log.d("Gans", "Loading frogs")
        withContext(Dispatchers.IO) {
            frogs.value.clear()
            frogs.value.addAll(RealmHelper.getAllFrog())
            Log.d("Gans", "Frogs loaded : ${frogs.value.count()}")
            //frogs. = RealmHelper.getAllFrog()
        }
    }

    Column(modifier = Modifier.padding(15.dp)) {
        Row {
            Button(modifier = Modifier.wrapContentSize(), onClick = {
                coroutineScope.launch {
                    withContext(Dispatchers.IO) {
                        try {
                            val frogCount = RealmHelper.getFrogCount() + 1
                            val frog = Frog()
                            frog.name = "Frog$frogCount"
                            frog.age = 10 + frogCount
                            val pond = Pond()
                            pond.name = "Pond$frogCount"
                            frog.pond = pond
                            val data = RealmHelper.saveFrog(frog)
                            println(data.toString())
                            frogs.value.clear()
                            frogs.value.addAll(RealmHelper.getAllFrog())
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
                        RealmHelper.clearAllFrog()
                        frogs.value.clear()
                    }
                }
            }) {
                Text(text = "Clear Frog")
            }
        }

        LazyColumn(
            contentPadding = PaddingValues(8.dp)
        ) {
            items(frogs.value) { item ->
                Text(
                    text = "Frog:".plus(item.name).plus(" Age:").plus(item.age).plus(" Pond:")
                        .plus(item.pond?.name)
                )
                Divider()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RealmExampleTheme {
        Greeting()
    }
}
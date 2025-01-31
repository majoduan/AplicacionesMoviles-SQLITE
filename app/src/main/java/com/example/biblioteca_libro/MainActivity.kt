package com.example.biblioteca_libro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.biblioteca_libro.model.Biblioteca
import com.example.biblioteca_libro.ui.theme.BibliotecaLibroTheme

import com.example.biblioteca_libro.model.BibliotecaEntity
import com.example.biblioteca_libro.model.BibliotecaDatabase
import com.example.biblioteca_libro.model.BibliotecaDao
import com.example.biblioteca_libro.model.LibroDao
import com.example.biblioteca_libro.model.LibroEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = BibliotecaDatabase.getDatabase(this)
        val bibliotecaDao = database.bibliotecaDao()
        val libroDao = database.libroDao()

        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "bibliotecas") {

                // 📌 Mostrar lista de bibliotecas
                composable("bibliotecas") {
                    BibliotecaList(navController, bibliotecaDao)
                }

                // 📌 Mostrar lista de libros en una biblioteca específica
                composable("libros/{bibliotecaId}") { backStackEntry ->
                    val bibliotecaId = backStackEntry.arguments?.getString("bibliotecaId")?.toInt() ?: 0
                    LibroList(bibliotecaId, navController, libroDao)
                }

                // 📌 Crear una nueva biblioteca
                composable("crearBiblioteca") {
                    CrearBibliotecaScreen(bibliotecaDao, navController)
                }

                // 📌 Editar una biblioteca existente
                composable("editarBiblioteca/{bibliotecaId}") { backStackEntry ->
                    val bibliotecaId = backStackEntry.arguments?.getString("bibliotecaId")?.toInt() ?: 0
                    EditarBibliotecaScreen(bibliotecaId, bibliotecaDao, navController)
                }


                // 📌 Crear un nuevo libro en una biblioteca específica
                composable("crearLibro/{bibliotecaId}") { backStackEntry ->
                    val bibliotecaId = backStackEntry.arguments?.getString("bibliotecaId")?.toInt() ?: 0
                    CrearLibroScreen(bibliotecaId, libroDao, navController)
                }

                // 📌 Editar un libro existente
                composable("editarLibro/{bibliotecaId}/{libroId}") { backStackEntry ->
                    val bibliotecaId = backStackEntry.arguments?.getString("bibliotecaId")?.toInt() ?: 0
                    val libroId = backStackEntry.arguments?.getString("libroId") ?: ""
                    EditarLibroScreen(libroId, bibliotecaId, libroDao, navController)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BibliotecaList(navController: NavHostController, bibliotecaDao: BibliotecaDao) {
    val bibliotecas by bibliotecaDao.getAllBibliotecas().collectAsState(initial = emptyList())

    Scaffold(
        topBar = { SmallTopAppBar(title = { Text("Bibliotecas") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("crearBiblioteca")
            }) {
                Text("+")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding).fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(bibliotecas) { biblioteca ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(biblioteca.nombre, style = MaterialTheme.typography.titleMedium)
                            Text(biblioteca.direccion, style = MaterialTheme.typography.bodyMedium)
                        }
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Button(onClick = {
                                navController.navigate("libros/${biblioteca.id}")
                            }) {
                                Text("Ver")
                            }
                            Button(onClick = {
                                navController.navigate("editarBiblioteca/${biblioteca.id}")
                            }) {
                                Text("Editar")
                            }
                            Button(onClick = {
                                CoroutineScope(Dispatchers.IO).launch {
                                    bibliotecaDao.deleteBiblioteca(biblioteca)
                                }
                            }) {
                                Text("Eliminar")
                            }
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibroList(
    bibliotecaId: Int,
    navController: NavHostController,
    libroDao: LibroDao
) {
    val libros by libroDao.getLibrosByBiblioteca(bibliotecaId).collectAsState(initial = emptyList())

    Scaffold(
        topBar = { SmallTopAppBar(title = { Text("Libros") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("crearLibro/$bibliotecaId")
            }) {
                Text("+")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding).fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(libros) { libro ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("${libro.titulo} - ${libro.autor}")
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(onClick = {
                                navController.navigate("editarLibro/${libro.id}")
                            }) {
                                Text("Editar")
                            }
                            Button(onClick = {
                                CoroutineScope(Dispatchers.IO).launch {
                                    libroDao.deleteLibro(libro)
                                }
                            }) {
                                Text("Eliminar")
                            }
                        }
                    }
                }
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearBibliotecaScreen(bibliotecaDao: BibliotecaDao, navController: NavHostController) {
    var nombre by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var fechaInauguracion by remember { mutableStateOf("") }
    var abiertaAlPublico by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { SmallTopAppBar(title = { Text("Nueva Biblioteca") }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
            TextField(value = direccion, onValueChange = { direccion = it }, label = { Text("Dirección") })
            TextField(value = fechaInauguracion, onValueChange = { fechaInauguracion = it }, label = { Text("Fecha de Inauguración (YYYY-MM-DD)") })
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = abiertaAlPublico, onCheckedChange = { abiertaAlPublico = it })
                Text("Abierta al Público")
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(onClick = { navController.popBackStack() }) {
                    Text("Cancelar")
                }
                Button(onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        bibliotecaDao.insertBiblioteca(
                            BibliotecaEntity(
                                nombre = nombre,
                                direccion = direccion,
                                fechaInauguracion = fechaInauguracion,
                                abiertaAlPublico = abiertaAlPublico
                            )
                        )
                    }
                    navController.popBackStack()
                }) {
                    Text("Guardar")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarBibliotecaScreen(
    bibliotecaId: Int,
    bibliotecaDao: BibliotecaDao,
    navController: NavHostController
) {
    // Obtener la biblioteca desde la base de datos usando bibliotecaId
    val biblioteca by bibliotecaDao.getBibliotecaById(bibliotecaId).collectAsState(initial = null)

    // Si la biblioteca aún no se ha cargado, mostramos un indicador de carga
    if (biblioteca == null) {
        CircularProgressIndicator()
        return
    }

    var nombre by remember { mutableStateOf(biblioteca!!.nombre) }
    var direccion by remember { mutableStateOf(biblioteca!!.direccion) }
    var fechaInauguracion by remember { mutableStateOf(biblioteca!!.fechaInauguracion) }
    var abiertaAlPublico by remember { mutableStateOf(biblioteca!!.abiertaAlPublico) }

    Scaffold(
        topBar = { SmallTopAppBar(title = { Text("Editar Biblioteca") }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") }
            )
            TextField(
                value = direccion,
                onValueChange = { direccion = it },
                label = { Text("Dirección") }
            )
            TextField(
                value = fechaInauguracion,
                onValueChange = { fechaInauguracion = it },
                label = { Text("Fecha de Inauguración (YYYY-MM-DD)") }
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = abiertaAlPublico,
                    onCheckedChange = { abiertaAlPublico = it }
                )
                Text("Abierta al Público")
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(onClick = {
                    navController.popBackStack()
                }) {
                    Text("Cancelar")
                }
                Button(onClick = {
                    val bibliotecaEditada = biblioteca!!.copy(
                        nombre = nombre,
                        direccion = direccion,
                        fechaInauguracion = fechaInauguracion,
                        abiertaAlPublico = abiertaAlPublico
                    )
                    // Guardar en base de datos usando una corrutina
                    kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
                        bibliotecaDao.updateBiblioteca(bibliotecaEditada)
                    }
                    navController.popBackStack()
                }) {
                    Text("Guardar")
                }
            }
        }
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearLibroScreen(
    bibliotecaId: Int,
    libroDao: LibroDao,
    navController: NavHostController
) {
    var titulo by remember { mutableStateOf("") }
    var autor by remember { mutableStateOf("") }

    Scaffold(
        topBar = { SmallTopAppBar(title = { Text("Nuevo Libro") }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = { Text("Título del Libro") }
            )
            TextField(
                value = autor,
                onValueChange = { autor = it },
                label = { Text("Autor del Libro") }
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(onClick = {
                    navController.popBackStack()
                }) {
                    Text("Cancelar")
                }
                Button(onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        libroDao.insertLibro(LibroEntity(bibliotecaId = bibliotecaId, titulo = titulo, autor = autor))
                    }
                    navController.popBackStack()
                }) {
                    Text("Guardar")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarLibroScreen(
    libroId: String, // ✅ Asegurar que recibe un String
    bibliotecaId: Int,
    libroDao: LibroDao,
    navController: NavHostController
) {
    val libro by libroDao.getLibroById(libroId).collectAsState(initial = null)

    if (libro == null) {
        CircularProgressIndicator()
        return
    }

    var titulo by remember { mutableStateOf(libro!!.titulo) }
    var autor by remember { mutableStateOf(libro!!.autor) }

    Scaffold(
        topBar = { SmallTopAppBar(title = { Text("Editar Libro") }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = { Text("Título del Libro") }
            )
            TextField(
                value = autor,
                onValueChange = { autor = it },
                label = { Text("Autor del Libro") }
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(onClick = { navController.popBackStack() }) {
                    Text("Cancelar")
                }
                Button(onClick = {
                    val libroEditado = libro!!.copy(
                        titulo = titulo,
                        autor = autor
                    )
                    CoroutineScope(Dispatchers.IO).launch {
                        libroDao.updateLibro(libroEditado)
                    }
                    navController.popBackStack()
                }) {
                    Text("Guardar")
                }
            }
        }
    }
}


package com.example.empresa_empleado

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*

import com.example.empresa_empleado.model.EmpresaEntity
import com.example.empresa_empleado.model.EmpresaDatabase
import com.example.empresa_empleado.model.EmpresaDao
import com.example.empresa_empleado.model.EmpleadoDao
import com.example.empresa_empleado.model.EmpleadoEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = EmpresaDatabase.getDatabase(this)
        val empresaDao = database.empresaDao()
        val empleadoDao = database.empleadoDao()

        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "empresas") {

                // 📌 Mostrar lista de empresas
                composable("empresas") {
                    EmpresaList(navController, empresaDao)
                }

                // 📌 Mostrar lista de empleados en una empresa específica
                composable("empleados/{empresaId}") { backStackEntry ->
                    val empresaId = backStackEntry.arguments?.getString("empresaId")?.toInt() ?: 0
                    EmpleadoList(empresaId, navController, empleadoDao)
                }

                // 📌 Crear una nueva empresa
                composable("crearEmpresa") {
                    CrearEmpresaScreen(empresaDao, navController)
                }

                // 📌 Editar una empresa existente
                composable("editarEmpresa/{empresaId}") { backStackEntry ->
                    val empresaId = backStackEntry.arguments?.getString("empresaId")?.toInt() ?: 0
                    EditarEmpresaScreen(empresaId, empresaDao, navController)
                }


                // 📌 Crear un nuevo empleado en una empresa específica
                composable("crearEmpleado/{empresaId}") { backStackEntry ->
                    val empresaId = backStackEntry.arguments?.getString("empresaId")?.toInt() ?: 0
                    CrearEmpleadoScreen(empresaId, empleadoDao, navController)
                }

                // 📌 Editar un empleado existente
                composable("editarEmpleado/{empresaId}/{empleadoId}") { backStackEntry ->
                    val empresaId = backStackEntry.arguments?.getString("empresaId")?.toInt() ?: 0
                    val empleadoId = backStackEntry.arguments?.getString("empleadoId") ?: ""
                    EditarEmpleadoScreen(empleadoId, empresaId, empleadoDao, navController)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmpresaList(navController: NavHostController, empresaDao: EmpresaDao) {
    val empresas by empresaDao.getAllEmpresas().collectAsState(initial = emptyList())

    Scaffold(
        topBar = { SmallTopAppBar(title = { Text("Empresas") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("crearEmpresa")
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
            items(empresas) { empresa ->
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
                            Text(empresa.nombre, style = MaterialTheme.typography.titleMedium)
                            Text(empresa.direccion, style = MaterialTheme.typography.bodyMedium)
                            Text(empresa.fechaFundacion, style = MaterialTheme.typography.bodyMedium)
                            Text("$ ${empresa.ingresoAnual.toString()}", style = MaterialTheme.typography.bodyMedium)
                            Text(text = if (empresa.esMultinacional) "Internacional" else "Nacional",
                                style = MaterialTheme.typography.bodyMedium)
                        }
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Button(onClick = {
                                navController.navigate("empleados/${empresa.id}")
                            }) {
                                Text("Ver Empleados")
                            }
                            Button(onClick = {
                                navController.navigate("editarEmpresa/${empresa.id}")
                            }) {
                                Text("Editar")
                            }
                            Button(onClick = {
                                CoroutineScope(Dispatchers.IO).launch {
                                    empresaDao.deleteEmpresa(empresa)
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
fun EmpleadoList(
    empresaId: Int,
    navController: NavHostController,
    empleadoDao: EmpleadoDao
) {
    val empleados by empleadoDao.getEmpleadosByEmpresa(empresaId).collectAsState(initial = emptyList())

    Scaffold(
        topBar = { SmallTopAppBar(title = { Text("Empleados") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("crearEmpleado/$empresaId")
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
            items(empleados) { empleado ->
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
                        Text("${empleado.nombre} - ${empleado.departamento}- ${empleado.salario}")
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(onClick = {
                                navController.navigate("editarEmpleado/${empleado.id}")
                            }) {
                                Text("Editar")
                            }
                            Button(onClick = {
                                CoroutineScope(Dispatchers.IO).launch {
                                    empleadoDao.deleteEmpleado(empleado)
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
fun CrearEmpresaScreen(empresaDao: EmpresaDao, navController: NavHostController) {
    var nombre by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var fechaFundacion by remember { mutableStateOf("") }
    var ingresoAnual by remember { mutableDoubleStateOf(0.0) }
    var esMultinacional by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { SmallTopAppBar(title = { Text("Nueva Empresa") }) }
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
            TextField(value = fechaFundacion, onValueChange = { fechaFundacion = it }, label = { Text("Fecha de Fundación (YYYY-MM-DD)") })
            TextField(value = ingresoAnual.toString(),onValueChange = {
                    if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*$"))) {
                        ingresoAnual = it.toDoubleOrNull() ?: 0.0
                    }
                },
                label = { Text("Ingreso Anual") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = esMultinacional, onCheckedChange = { esMultinacional = it })
                Text("Es Multinacional")
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
                        empresaDao.insertEmpresa(
                            EmpresaEntity(
                                nombre = nombre,
                                direccion = direccion,
                                fechaFundacion = fechaFundacion,
                                ingresoAnual = ingresoAnual,
                                esMultinacional = esMultinacional
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
fun EditarEmpresaScreen(
    empresaId: Int,
    empresaDao: EmpresaDao,
    navController: NavHostController
) {
    // Obtener la empresa desde la base de datos usando empresaaId
    val empresa by empresaDao.getEmpresaById(empresaId).collectAsState(initial = null)

    // Si la empresa aún no se ha cargado, mostramos un indicador de carga
    if (empresa == null) {
        CircularProgressIndicator()
        return
    }

    var nombre by remember { mutableStateOf(empresa!!.nombre) }
    var direccion by remember { mutableStateOf(empresa!!.direccion) }
    var fechaFundacion by remember { mutableStateOf(empresa!!.fechaFundacion) }
    var ingresoAnual by remember { mutableDoubleStateOf(empresa!!.ingresoAnual) }
    var esMultinacional by remember { mutableStateOf(empresa!!.esMultinacional) }

    Scaffold(
        topBar = { SmallTopAppBar(title = { Text("Editar Empresa") }) }
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
                value = fechaFundacion,
                onValueChange = { fechaFundacion = it },
                label = { Text("Fecha de Inauguración (YYYY-MM-DD)") }
            )
            TextField(
                value = ingresoAnual.toString(),onValueChange = {
                if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*$"))) {
                    ingresoAnual = it.toDoubleOrNull() ?: 0.0
                }
            },
                label = { Text("Ingreso Anual") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = esMultinacional,
                    onCheckedChange = { esMultinacional = it }
                )
                Text("Es multinacional")
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
                    val empresaEditada = empresa!!.copy(
                        nombre = nombre,
                        direccion = direccion,
                        fechaFundacion = fechaFundacion,
                        ingresoAnual = ingresoAnual,
                        esMultinacional = esMultinacional
                    )
                    // Guardar en base de datos usando una corrutina
                    kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
                        empresaDao.updateEmpresa(empresaEditada)
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
fun CrearEmpleadoScreen(
    empresaId: Int,
    empleadoDao: EmpleadoDao,
    navController: NavHostController
) {
    var nombre by remember { mutableStateOf("") }
    var departamento by remember { mutableStateOf("") }
    var salario by remember { mutableDoubleStateOf(0.0) }

    Scaffold(
        topBar = { SmallTopAppBar(title = { Text("Nuevo Empleado") }) }
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
                label = { Text("Nombre del Empleado") }
            )
            TextField(
                value = departamento,
                onValueChange = { departamento = it },
                label = { Text("Departamento del Empleado") }
            )
            TextField(
                value = salario.toString(),onValueChange = {
                    if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*$"))) {
                        salario = it.toDoubleOrNull() ?: 0.0
                    }
                },
                label = { Text("Salario") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
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
                        empleadoDao.insertEmpleado(EmpleadoEntity(empresaId = empresaId, nombre = nombre, departamento = departamento, salario = salario))
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
fun EditarEmpleadoScreen(
    empleadoId: String, // ✅ Asegurar que recibe un String
    empresaId: Int,
    empleadoDao: EmpleadoDao,
    navController: NavHostController
) {
    val empleado by empleadoDao.getEmpleadoById(empleadoId).collectAsState(initial = null)

    if (empleado == null) {
        CircularProgressIndicator()
        return
    }

    var nombre by remember { mutableStateOf(empleado!!.nombre) }
    var departamento by remember { mutableStateOf(empleado!!.departamento) }
    var salario by remember { mutableStateOf(empleado!!.salario) }


    Scaffold(
        topBar = { SmallTopAppBar(title = { Text("Editar Empleado") }) }
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
                label = { Text("Nombre del Empleado") }
            )
            TextField(
                value = departamento,
                onValueChange = { departamento = it },
                label = { Text("Departamento del Empleado") }
            )
            TextField(
                value = salario.toString(),onValueChange = {
                    if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*$"))) {
                        salario = it.toDoubleOrNull() ?: 0.0
                    }
                },
                label = { Text("Salario") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(onClick = { navController.popBackStack() }) {
                    Text("Cancelar")
                }
                Button(onClick = {
                    val empleadoEditado = empleado!!.copy(
                        nombre = nombre,
                        departamento = departamento,
                        salario = salario
                    )
                    CoroutineScope(Dispatchers.IO).launch {
                        empleadoDao.updateEmpleado(empleadoEditado)
                    }
                    navController.popBackStack()
                }) {
                    Text("Guardar")
                }
            }
        }
    }
}


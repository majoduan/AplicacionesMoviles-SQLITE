### Deber 1 y 2
Enlace del video: https://epnecuador-my.sharepoint.com/:v:/g/personal/mateo_duenas_epn_edu_ec/EcUr08NsrihDj0PZL1ipfSwB9fyBYfE5-whvrOyX10QFcw?nav=eyJyZWZlcnJhbEluZm8iOnsicmVmZXJyYWxBcHAiOiJTdHJlYW1XZWJBcHAiLCJyZWZlcnJhbFZpZXciOiJTaGFyZURpYWxvZy1MaW5rIiwicmVmZXJyYWxBcHBQbGF0Zm9ybSI6IldlYiIsInJlZmVycmFsTW9kZSI6InZpZXcifX0%3D&e=BIjNhI

### Enlace a informes de las clases:
https://epnecuador-my.sharepoint.com/:o:/g/personal/mateo_duenas_epn_edu_ec/Ehiw_g4rWYhPnKjfxfVgDVEBfBhJybC9Hdz3DjV_8JxFGA?email=adrian.eguez%40epn.edu.ec&e=pt5fXo

# 🎵 song-microservice-container

Microservicio RESTful para gestión de canciones utilizando Node.js y SQL Server, desplegado en Azure Container Apps.

## 🚀 Tecnologías Utilizadas
- 🟢 Node.js
- ⚡ Express.js
- 🗄️ Microsoft SQL Server
- 🐳 Docker
- ☁️ Azure Container Apps
- 🏛️ Azure SQL Database

## 📜 Descripción del Proyecto
Este proyecto implementa un CRUD de canciones que permite realizar operaciones como crear, leer, actualizar y eliminar canciones almacenadas en una base de datos SQL Server. Se utiliza un enfoque basado en microservicios y se despliega en la nube con Azure Container Apps.

## 📂 Estructura del Proyecto
song-microservice-container 
```plaintext
│── src 
│ ├── config 
│ │ ├── database.js 
│ ├── controllers 
│ │ ├── songController.js 
│ ├── models 
│ │ ├── song.js 
│ ├── routes 
│ │ ├── songRoutes.js 
│ ├── app.js 
│── .env 
│── docker-compose.yml 
│── Dockerfile 
│── package.json 
│── package-lock.json 
│── README.md
```

## 🔗 Endpoints Disponibles
El microservicio expone los siguientes endpoints:

### 📥 Obtener todas las canciones
GET /api/songs
**Ejemplo de respuesta:**
```json
[
  {
    "Id": 1,
    "Name": "Sunrise",
    "Path": "../example/songs/sunrise.mp3",
    "Plays": 0
  },
  {
    "Id": 2,
    "Name": "Chill Vibes",
    "Path": "../example/songs/chill-vibes.mp3",
    "Plays": 1
  }
]
```
## 🔍 Obtener una canción por ID
GET /api/songs/:id

## ➕ Crear una nueva canción
POST /api/songs
**Ejemplo de respuesta:**
```json
[
{
  "Name": "New Song",
  "Path": "../example/songs/new-song.mp3",
  "Plays": 0
}
]
```
## ✏️ Actualizar una canción
PUT /api/songs/:id

## 🗑️ Eliminar una canción
DELETE /api/songs/:id

##⚙️ Configuración y Ejecución
### 📌 Requisitos Previos
- Tener instalado Node.js
- Tener configurado Microsoft SQL Server
- Tener instalado Docker

## 📦 Instalación
### Clona el repositorio:
git clone https://github.com/tuusuario/song-microservice-container.git
### Accede al directorio del proyecto:
cd song-microservice-container
### Instala las dependencias:
npm install

## 🔑 Configuración del Entorno
Crea un archivo .env en la raíz del proyecto con la configuración de la base de datos:
DB_HOST=tu_servidor_sql
DB_USER=tu_usuario
DB_PASSWORD=tu_contraseña
DB_NAME=tu_base_de_datos

## 🏗️ Ejecución en Desarrollo
Para iniciar el servidor en modo desarrollo:
npm run dev

## 🐳 Ejecutar con Docker
Para construir y ejecutar el contenedor:
docker build -t song-microservice .
docker run -p 3000:3000 song-microservice

## ☁️ Despliegue en Azure
az containerapp up --name song-microservice --resource-group tu-grupo --image song-microservice:latest

## 📜 Licencia
Este proyecto está bajo la licencia MIT.

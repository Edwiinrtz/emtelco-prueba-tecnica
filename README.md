# Prueba técnica - Desarrollador CRM - Mobile

## Objetivo

Desarrollar una aplicación móvil (stack a libre elección) que cuente con 2 pantallas:

- Catálogo de Pokémon
- Carrito de compras

### Requisitos

- Cada ítem del catálogo puede agregarse al carrito.
- Simular los precios de los Pokémon.
- Poder eliminar elementos del carrito.
- Posibilidad de trabajar sin conexión.

## Instalación

Clonar el proyecto desde GitHub:

```
git clone https://github.com/Edwiinrtz/emtelco-prueba-tecnica.git
```


O, si es su preferencia, descargar el archivo .zip:  
https://github.com/Edwiinrtz/emtelco-prueba-tecnica/archive/refs/heads/main.zip

Este descargará un archivo llamado *emtelco-prueba-tecnica-main.zip*. Descomprímalo y en ambos casos encontrará una carpeta llamada *emtelco-prueba-tecnica* o *emtelco-prueba-tecnica-main*.

Ejecute Android Studio y abra la carpeta del proyecto.

## Ejecución del proyecto

Puede ejecutar el proyecto en emuladores virtuales; para ello, desde el IDE debe crear uno en la sección:

```
Device Manager > Add a new device > Create Virtual Device
```

Podrá seleccionar el hardware preferido, pero es obligatorio que la versión de Android sea como mínimo **ANDROID 13** o **API 33**.

También puede ejecutar el software en dispositivos Android físicos, respetando igualmente las versiones mínimas **ANDROID 13** o **API 33** del sistema.

## Arquitectura

Para el proyecto se optó por utilizar una arquitectura **MVVM** - Model - View - ViewModel.

![image mvvm](https://user-images.githubusercontent.com/1812129/68319232-446cf900-00be-11ea-92cf-cad817b2af2c.png)

Se opta por esta arquitectura para el desarrollo del aplicativo porque, en su esencia, está muy bien definido el rol de cada una de sus capas, lo que hace que el código sea más limpio y fácil de mantener.

También facilita la actualización automática de la interfaz por acciones del usuario o del sistema.

## Árbol de carpetas
```
kotlin+java/
└── com.emtelco.pokeapiemtelco/
    ├── core/
    ├── data/
    │   ├── dba/
    │   ├── model/
    │   └── network/
    ├── di/
    ├── view/
    │   ├── components/
    │   ├── screens/
    │   └── ui.theme/
    ├── viewmodel/
    ├── MainActivity
    └── MyHiltApp
```

- **core**: Encontrará una serie de *helpers* o clases creadas para definir parte de la lógica que será inyectada en los *ViewModels* para el funcionamiento del aplicativo.

- **data**: Contiene *entities*, modelos e interfaces relativas a la obtención y disposición de los datos en el sistema.

- **di**: *Dependency Injection*. Contiene los módulos usados por *Dagger Hilt* para gestionar la inyección de dependencias en el sistema.

- **view**: Todo lo relativo a las vistas, componentes, pantallas y *themes*.

- **viewmodel**: *ViewModels* para cada pantalla del aplicativo.

## Funciones nativas

- **Vibración o *feedback* háptico**: Esta función permite al usuario estar seguro sobre una acción que no es muy visible en el sistema. Fue implementada específicamente al momento de agregar un Pokémon al carrito de compra y al eliminarlo.

- **Notificaciones**: Mejoran la experiencia e interacción del usuario en el uso del sistema. Alertan sobre el estado de conexión a internet o cuando una compra de Pokémon finaliza.

- **Sensor de huella o contraseña**: Al simular una compra dentro de la aplicación, el componente de seguridad es un plus llamativo para el usuario.

## Testing

Se realizaron pruebas unitarias para los *ViewModels* únicamente, pues en ellos se centra la mayoría de la lógica implementada.

Para ejecutar los tests unitarios, puede hacerse desde la consola con Gradle o ejecutar la tarea Gradle con el comando:
```
gradle test
```
O individualmente desde los archivos:
```
package com.emtelco.pokeapiemtelco.viewmodel

├── PokemonCartViewModelTest
└── PokemonListViewModelTest
```

## Análisis visual

#### Dashboard

El diseño de la aplicación tiene un enfoque minimalista y sencillo. En la primera pantalla encontrará un *grid* de 2 columnas donde verá listado cada uno de los Pokémon disponibles. Un **clic** o **tap** sobre cada Pokémon lo agregará al carrito solo 1 vez.

En la zona inferior derecha de la pantalla hay un botón que lo dirigirá a la segunda pantalla.

<img src="https://lh3.googleusercontent.com/d/1IwByeqiky18JLMywzdMzc2PXEkcEMSsS" alt="dashboard" height="600"/>

#### Carrito de compra

El carrito de compra cuenta con la opción de regresar al menú principal, el listado de ítems por comprar y, en la parte inferior, botón de pago y total a pagar.

Cada uno de los ítems tiene la opción de aumentar la cantidad a comprar; cada cambio actualiza el costo individual y total. Se puede eliminar un ítem si se presiona el cesto de basura correspondiente.

<img src="https://lh3.googleusercontent.com/d/1PUXoOxT6p9GNQMg7XPGZ5ekJe1l0OSMd" alt="dashboard" height="600"/>

### Video de presentación

https://drive.google.com/file/d/1c-gc7LjHgOciLixhrU-VR-b0oEklVsZs/view?usp=sharing

### Link APK

<a href="https://www.mediafire.com/file/pmf2agdw9oduqjh/poke-emtelco.apk/file">Descargar</a>
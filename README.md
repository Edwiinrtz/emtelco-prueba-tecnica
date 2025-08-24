
# Prueba tecnica - desarrollador CRM - mobile

## Objetivo

Desarrollar aplicacion mobile (stack a libre eleccion) que cuente con 2 pantallas:

- Catalogo de pokemons
- Carrito de compras

### Requisitos

- Cada item del catalogo puede agregarse al carrito.
- Simular los precios de los pokemons
- Poder eliminar elemento del carrito
- Posibilidad de trabajar sin conexión


## Instalacion

Clonar el proyecto desde github:

```
git clone https://github.com/Edwiinrtz/emtelco-prueba-tecnica.git

```

O si es su preferencia descargar el archivo .zip:  https://github.com/Edwiinrtz/emtelco-prueba-tecnica/archive/refs/heads/main.zip

Este descargará un archivo llamado *emtelco-prueba-tecnica-main.zip* descomprima y en ambos casos encontrará una carpeta llamada  *emtelco-prueba-tecnica* o *emtelco-prueba-tecnica-main*

Ejecute Android Studio y abra la carpeta del proyecto.

## Ejecución del proyecto

Puede ejecutar el proyecto en emuladores virtuales, para ello desde el IDE debe crear uno en la sección

```
    Device manager > Add a new device > Create virtual device
```

Podrá selecccionar el hardware preferido pero es obligatorio que la version de Android sea como minimo __ANDROID 13__ o __API 33__

Tambien puede ejecutar el software en dispositivos android fisicamente. Tambien respetando las versiones minimas __ANDROID 13__ o __API 33__ del sistema.


## Arquitectura

Para el proyecto se optó por utilizar una Arquitectura **mvvm**  -  Model - View - ViewModel

![image mvvm](https://user-images.githubusercontent.com/1812129/68319232-446cf900-00be-11ea-92cf-cad817b2af2c.png)

Se opta por esta Arquitectura  para el desarrollo del aplicativo porque en su escencia está muy bien definido el rol de cada una de sus capaz, lo que hace que el codigo sea más limpio y facil de mantener.

Tambien facilita la actualización automatica de la interfaz por acciones del usuario o del sistema.

## Arbol de carpetas

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

- **core**: Encontrará una serie de helpers o clases creadas para definir parte de la logica que serpa inyectada en los viewmodels para funcionamiento del aplicativo.

- __data__: Contiene entities, modelos e interfaces relativa a las obtencion y disposición de los datos en el sistema.

- __di__: *dependency injection* Contiene los modulos usados por *Dagger hilt* para gestionar la inyección de dependencias en el sistema.

- __view__: Todo lo relativo a las vistas, componentes, pantallas y themes.

- __viewmodel__: ViewModels para cada pantalla del aplicativo.

## Funciones nativas

- __Vibración o feedback háptico__: Está funcion permite al usuario estar seguro sobre una acción que no es muy visible en el sistema. Fue implementada especificamente al momento de agregar un pokemon al carrito de compra y al eliminarlo del carrito de compra.

- __Notificaciones__: Mejora la experiencia e intreracción de usuario en el uso del sistema. Alerta sobre el estado de conexion a internet o cuando una compra de pokemons finaliza.

- __Sensor de huella o contraseña__: Al simular una compra dentro de la aplicación, el componente de seguridad es un plus llamativo para el usuario.

## Testing

Se realizaron pruebas unitarias par los viewModel unicamente, pues en ellos se sentra la mayoria de logica implementada.

para ejecutar los test unitatiros puede hacerse desde console gradle  o execute gradle task con el comando.

```
 gradle test
```

o individualmente desde los archivos

```
package com.emtelco.pokeapiemtelco.viewmodel

├── PokemonCartViewModelTest
└── PokemonListViewModelTest

```
## Analisis visual

#### dashboard

El diseño de la aplicación tiene un enfoque minimalista y sencillo. en la primer pantalla encontrará un grid de 2 columnas donde verá listado cada uno de los pokemon disponibles. __clic__ o __tap__ sombre cada pokemon lo agregará al carrito solo 1 vez.

En la zona inferio derecha de la pantalla hay un botón que lo dirigé a la segunda pantalla.

<img src="https://lh3.googleusercontent.com/d/1IwByeqiky18JLMywzdMzc2PXEkcEMSsS" alt="dashboard" height="600"/>


#### Carrito de compra

El carrito de compra cuenta con la opcion de regresar al menu principal, el listado de items por comprar y en la parte inferior boton de pago y total a pagar.

Cada uno de los item tiene la opcion de aumentar la cantidad de items a comprar, cada cambio actualiza el costo individual y total. Se puedeeliminar un item si se presiona el cesto de basura correspondiente a sí mismo.

<iframe src="https://drive.google.com/file/d/1c-gc7LjHgOciLixhrU-VR-b0oEklVsZs/preview" width="640" height="480" allowfullscreen></iframe>

### link apk

<a href="https://www.mediafire.com/file/pmf2agdw9oduqjh/poke-emtelco.apk/file" > descargar </a>

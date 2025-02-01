# Documentación del despligue de la aplicación


## Indice

1. [Introducción](#id1)<br>
2. [Proceso de despliegue](#id2)<br>
3. [Paso 1: Dockerfile](#id3)<br>
4. [Paso 2:  Dockercompose](#id4)<br>
5. [Paso 3: Creación de instancia en Google Cloud](#id5)<br>
6. [Paso 4: Asignación de IP fija](#id6)<br>
7. [Paso 5: Asignación de dominio](#id7)<br>
8. [Paso 6: Configuración de firewall](#id8)<br>
9. [Paso 7: Instalación de Git y Docker](#id9)<br>
10. [Paso 8: Clonación del repositorio](#id10)<br>
11. [Paso 9: Levantar el programa ](#id11)<br>
12. [Modificaciones realizadas en nuestra aplicación](#id12)<br>

## Introducción <a id="id1"></a>

En esta documentación se explica todo el proceso de despligue de la aplicación de biblioteca en un servidor web, usando GCP (Google Cloud Plataform).

## Proceso de despligue <a id="id2"></a>

Para la implementación de la aplicación se ha utilizado un Docker. Se trata de una herramiento que se utiliza para la ejecución de aplicaciones en contenedores.

### Paso 1: Dockerfile <a id="id3"></a>

Comenzaremos creando el archivo llamandolo *Dockerfile*, sin necesidad de extensión. Una vez creado el archivo, dentro de el deberemos añadir el siguiente contenido.

```
FROM eclipse-temurin:23-jdk-alpine AS builder
WORKDIR /app
COPY . .
RUN chmod +x mvnw  #
RUN ./mvnw package -DskipTests

FROM eclipse-temurin:23-jdk-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
```
En el código superior se especifica el IDE desde el cual se contruye el docker, la carpeta desde la cual se trabajará en el contenedor, los permisos, el jdk de Java y otros elementos.<br>

### Paso 2: Dockercompose <a id="id4"></a>

En el archivo de *docker-compose* se utilizará para la base de datos en MySQL, la cual es la base de datos utilizada para el proyecto.

### Paso 3: Creación de instancia en Google Cloud <a id="id5"></a>

Haciendo uso de la prueba gratuita de Google Cloud se creó una máquina virtual la cual será la encargada de tener nuestra aplicación.<br>

Para la creación de la MV iremos, en nuestro GCP, al apartado de *Compute Engine* -> *Instancias de VM*. Una vez estamemos ahi le damos en *Crear instancia*.<br>

Al darle nos aparecerá las opciones de personalización de nuestra MV, el sistema operativo que usará nuestra maquina virtual es Ubuntu 20.04 LTS

![Crear maquina virtual](/imagenesDocu/crearInstancia.png)

Cuando este creada nos aparecera nuestra MV donde podremos trabajar con ella. Al hacer clic en ella nos aparecerá el botón de encender.

![Maquinas virtuales creadas](/imagenesDocu/listaInstancias.png)

### Paso 4: Asignación de IP fija <a id="id6"></a>

Para poder hacer uso de nuestra aplicación en el servidor web asignaremos una dirección IP fija, para hacerlo iremos al apartado de *Redes de VPC* -> *Direcciones IP*. Añadiremos una regla para que la dirección IP externa sea fija.

![Dirección ip](/imagenesDocu/direccionIpFija.png)

### Paso 5: Asignación de dominio <a id="id7"></a>

Vamos a añadirle un nombre de dominio (DNS) a nuestra aplicación, para ello usaremos la web de <a href="https://www.duckdns.org">duckdns.org</a> ya que es un sitio gratuito.

### Paso 6: Configuración de firewall <a id="id8"></a>

En este paso se modifica el firewall para que se habiliten los puerto 80 (http) y 25 (SMTP).

![Dirección ip](/imagenesDocu/firewall.png)

### Pase 7: Instalación de Git y Docker <a id="id9"></a>

A continuación, instalaremos en nuestra MV el git para clonar el repositorio de nuestra aplicación y el Docker.<br>

Lo primero que haremos es instalar Git, para ello ejecutaremos el siguiente comando en nuestra terminal:
```
sudo apt install git
```

> Se recomianda usar previamente el comando de `sudo apt update` para buscar posibles actualizaciones.

Una vez instalado comprobaremos que todo esta correcto mirando la versión, para ello usamos el comando:

```
git --version
```

Ahora instalaremos Docker, para ello usaremos el siguiente comando:
```
sudo apt install docker-ce
```

### Paso 8: Clonación del repositorio <a id="id10"></a>

Cuando ya tengamos el git instalado lo siguiente que haremos es clonar el repositorio, para ello, ejecutamos el siguiente comando en la terminal:
```
git clone (url del repositorio)
```
### Paso 9: Levantar el programa <a id="id11"></a>

El ultimo paso que realizaremos sera ejecutar el programa con el siguiente comando:
```
docker compose up --build
```

Si todo esta correcto nuestra aplicación se ejecutará sin problema alguno.

<hr>

## Modificaciones realizadas en nuestra aplicación <a id="id12"></a>

Durante el proceso de despligue de la aplicación fueron necesarias ciertas modificaciones para su correcto funcionamiento, las cuales se detallan a continuación:

- Modificación del properties para que TomCat use el `puerto 80` en lugar del `8080`.
- Modificación de ficheros html, hardcodeado el `localhost` para las imagenes, se modifico para que apunte al dominio de la página.
- Modificación del archivo sql con los libros por defecto, sustituyendo el `localhost` por el nombre de dominio.
- Modificación del properties en la conexión del MySQL, apuntando al contenedor MySQL del Docker. 
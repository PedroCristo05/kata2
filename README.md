# Kata 2 - Entrada, salida y carga de datos

El objetivo central de esta kata es practicar el manejo de flujos de entrada y salida mediante `InputStream` y `OutputStream`, estableciendo fronteras claras entre la obtención de datos externos, el procesamiento/transformación pura en memoria y la escritura hacia destinos desacoplados.

---

## Enlace al vídeo explicativo

* **Vídeo demostrativo:** [kata2_pcpc]([url](https://youtu.be/DewNpZs51gg))

---

## Entorno y Requisitos

* **Lenguaje:** Java 26
* **Gestor de construcción y dependencias:** Maven
* **Entorno de desarrollo:** IntelliJ IDEA
* **Control de versiones:** Git & GitHub

---

## Estructura del Proyecto

El código fuente se organiza siguiendo una arquitectura modular bajo el paquete base `software.ulpgc.katas`:

```text
src/main/java/software/ulpgc/katas/
├── model/
│   ├── Platform.java          # Enum con las plataformas admitidas y valor 'Other' de escape.
│   └── Game.java              # Record inmutable de dominio que modela una fila del dataset.
├── io/
│   ├── GameReader.java        # Interfaz de entrada para la carga de datos.
│   ├── FileGameReader.java    # Adaptador de entrada desde fichero local (FileInputStream).
│   ├── UrlGameReader.java     # Adaptador de red mediante streaming directo (URL.openStream).
│   ├── SummaryWriter.java     # Interfaz de salida desacoplada basada en OutputStream.
│   └── GameSummaryWriter.java # Adaptador de formateo y escritura mediante PrintWriter.
├── service/
│   └── GameStatistics.java    # Transformación pura en memoria (agrupación y conteo con Streams).
└── Main.java                  # Orquestador del flujo, demostración de destinos y casos borde.
```

## Dataset de prueba
vgsales.csv: Muestra reducida de datos tabulares delimitados por comas ubicada en la raíz del proyecto para pruebas reproducibles sin dependencias locales.

---

## Decisiones de Diseño y Desacoplamiento

1. **Inmutabilidad y Modelo:**  
   Se implementó `Game` como un `record` de Java para evitar código redundante (*boilerplate*), garantizar inmutabilidad en los registros leídos y proporcionar accesores canónicos limpios. El enum `Platform` protege la clasificación e incluye la constante `Other` para absorber consolas imprevistas.

2. **Reutilización del Parseo en Streams:**  
   El método `loadFrom(InputStream is)` de `FileGameReader` se definió con visibilidad pública, permitiendo que `UrlGameReader` delegue en él directamente el flujo de bytes de `url.openStream()`. De este modo, ambos orígenes de entrada comparten el 100% de la lógica de parseo, salto de cabecera y filtrado sin duplicar código.

3. **Lógica de Transformación Pura:**  
   `GameStatistics.countByPlatform` es una función estática y pura que recibe una colección de entidades en memoria y retorna un `Map<Platform, Long>` mediante `Collectors.groupingBy`. No abre conexiones ni archivos, desconociendo por completo de dónde proceden los datos o hacia dónde se enviarán.

4. **Escritura Desacoplada en OutputStream:**  
   `GameSummaryWriter` implementa la interfaz `SummaryWriter` recibiendo un `OutputStream` abstracto. Esto permite volcar el mismo resumen estructurado tanto a la consola estándar (`System.out`) como a un fichero en disco (`FileOutputStream`) sin alterar la lógica de presentación.

5. **Extensibilidad (Pregunta de Diseño):**  
   - Para añadir un **nuevo origen de datos** (ej. base de datos relacional, archivo ZIP o memoria): solo se requiere implementar la interfaz `GameReader`, manteniendo intactos el servicio y la salida.
   - Para añadir un **nuevo destino de datos** (ej. un socket TCP de red o un `ByteArrayOutputStream` en memoria): se emplea la interfaz `SummaryWriter` pasándole dicho stream destino sin modificar la transformación.

---

## Gestión Defensiva de Errores

Para evitar fallos opacos y caídas del programa con excepciones en tiempo de ejecución (`RuntimeException`), se incorporaron controles defensivos específicos:

- **Datos numéricos mal formados o vacíos:** Los métodos auxiliares `parseIntOrDefault` y `parseDoubleOrDefault` gestionan los valores `"N/A"` presentes en datasets reales asignando valores por defecto (`0` / `0.0`).
- **Filas incompletas:** Se descartan registros truncados con menos de 11 columnas y se filtran nulos mediante `.filter(game -> game != null)`.
- **Fichero inexistente o ruta rota:** `FileGameReader.load()` captura `FileNotFoundException`, emite un aviso descriptivo en consola y devuelve una lista vacía inmutable (`List.of()`).
- **Fallo de red o URL inválida:** `UrlGameReader.load()` captura `IOException`, informa al usuario del problema de conexión y retorna `List.of()`, permitiendo que la aplicación continúe su ejecución de manera segura.

---

## Repetición Deliberada y Flujo Git

Siguiendo el criterio de repetición deliberada para automatizar el uso del IDE y el control de versiones, la kata se completó en tres ramas independientes con alcance equivalente:

1. **`develop1` (Repetición 1):** Creación del repositorio, configuración inicial de `.gitignore`, diseño base del modelo `Game`/`Platform`, adaptadores y detección de casos borde en el parseo.
2. **`develop2` (Repetición 2):** Reconstrucción desde el commit inicial para consolidar atajos, corrección de desincronizaciones de salida en consola y adición de comprobaciones explícitas de rutas rotas.
3. **`develop3` (Repetición 3 / Demostración):** Sesión final grabada en vídeo con la integración completa de los componentes, verificación de errores controlados e integración en `master`.

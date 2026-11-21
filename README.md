# Reto de Automatización QA — Wompi (Nequi)

Pruebas automatizadas de integración vía API para las transacciones con **Nequi** en el ambiente sandbox de Wompi.

El proyecto está construido en **Java 21** con **Serenity BDD**, el patrón **Screenplay** y escenarios **BDD** escritos en español con Cucumber.

> Si quieres conocer la arquitectura, el patrón de diseño, el listado completo de escenarios y las decisiones que tomé, revisa el [REVIEW.md](REVIEW.md).

---

## ¿Qué necesitas antes de empezar?

- **Java 21** (o superior). Puedes comprobarlo con:
  ```bash
  java -version
  ```
- **Git**, para clonar el repositorio.
- **Conexión a internet**, porque las pruebas se ejecutan contra el sandbox de Wompi.
- **Las llaves sandbox** del comercio que vienen en el enunciado de la prueba.

No necesitas instalar Gradle: el proyecto trae su propio *wrapper* (`gradlew`), que descarga la versión correcta la primera vez que lo ejecutas.

---

## Paso a paso para ejecutar las pruebas

### 1. Clona el repositorio

```bash
git clone https://github.com/JessicaParraG/RetoWompi.git
cd RetoWompi
```

### 2. Configura tus variables de entorno

Las llaves **no** están en el código. Debes crear un archivo `.env` a partir de la plantilla `.env.example`:

**En Windows (PowerShell o CMD):**
```bash
copy .env.example .env
```

**En macOS / Linux / Git Bash:**
```bash
cp .env.example .env
```

Luego abre el archivo `.env` y reemplaza los valores de ejemplo (`pub_stagtest_...`, `prv_stagtest_...`, etc.) por las llaves sandbox de la prueba.

Estas son las variables que usa el proyecto:

| Variable | ¿Obligatoria? | Para qué sirve |
|---|---|---|
| `WOMPI_BASE_URL` | Sí | URL del sandbox de Wompi |
| `WOMPI_PUBLIC_KEY` | Sí | Llave pública del comercio: crea transacciones y obtiene los tokens de aceptación |
| `WOMPI_PRIVATE_KEY` | Sí | Llave privada del comercio: consultas que requieren autenticación privada |
| `WOMPI_INTEGRITY_KEY` | Sí | Llave para generar la firma de integridad de cada transacción |
| `WOMPI_EVENTS_KEY` | No | Llave de eventos (webhooks). Los escenarios actuales no la usan; queda documentada para futuras pruebas de eventos |
| `WOMPI_HTTP_TIMEOUT_SECONDS` | No (30 por defecto) | Tiempo máximo para conectar con Wompi y recibir cada respuesta, para que la ejecución no se quede colgada si falla la red |
| `WOMPI_POLLING_TIMEOUT_SECONDS` | No (30 por defecto) | Tiempo máximo que se espera a que una transacción Nequi termine de procesarse |
| `WOMPI_POLLING_INTERVAL_MILLIS` | No (2000 por defecto) | Cada cuánto se consulta el estado de la transacción mientras se espera |

> El archivo `.env` está en el `.gitignore`, así que tus llaves nunca se subirán al repositorio.
> Si prefieres, también puedes definir estas variables directamente en tu sistema operativo o en tu pipeline de CI; esas tienen prioridad sobre el `.env`.

### 3. Ejecuta las pruebas

**En Windows:**
```bash
gradlew.bat clean test
```

**En macOS / Linux / Git Bash:**
```bash
./gradlew clean test
```

La primera ejecución tarda un poco más porque descarga Gradle y las dependencias. Las siguientes toman menos de un minuto.

Al terminar verás algo como esto:

```
Creación de transacciones con Nequi > Crear transacción Nequi con datos válidos PASSED
Creación de transacciones con Nequi > Crear transacción sin acceptance_token PASSED
...
Consulta de transacciones con Nequi > Utilizar una credencial privada inválida PASSED

BUILD SUCCESSFUL
```

### 4. Revisa el reporte

Serenity genera un reporte con el detalle de cada escenario, cada paso y cada llamada al API (request y response). Ábrelo en tu navegador:

```
target/site/serenity/index.html
```

---

## Ejecutar solo algunos escenarios

Cada escenario tiene etiquetas, así que puedes ejecutar solo un grupo:

```bash
# Solo los escenarios más importantes (smoke)
./gradlew clean test -Dcucumber.filter.tags="@smoke"

# Solo los escenarios negativos
./gradlew clean test -Dcucumber.filter.tags="@negativo"

# Un escenario específico
./gradlew clean test -Dcucumber.filter.tags="@CP-05"

# Combinando etiquetas
./gradlew clean test -Dcucumber.filter.tags="@creacion and @negativo"
```

En Windows usa `gradlew.bat` en lugar de `./gradlew`.

| Etiqueta | Qué ejecuta |
|---|---|
| `@positivo` | Flujos felices: crear, aprobar y consultar una transacción |
| `@alterno` | Resultados de negocio válidos pero distintos, como una transacción declinada |
| `@negativo` | Solicitudes inválidas o no autorizadas que Wompi debe rechazar |
| `@smoke` | Los escenarios esenciales para una verificación rápida |
| `@creacion`, `@resultado`, `@consulta` | Escenarios agrupados por etapa de la transacción |
| `@CP-01` … `@CP-08` | Un escenario puntual |

---

## Escenarios automatizados

| ID | Tipo | Escenario |
|---|---|---|
| CP-01 | Positivo | Crear transacción Nequi con datos válidos |
| CP-02 | Negativo | Crear transacción sin acceptance_token |
| CP-03 | Negativo | Crear transacción sin datos obligatorios |
| CP-04 | Negativo | Crear transacción con firma de integridad inválida |
| CP-05 | Positivo | Completar transacción Nequi con resultado aprobado |
| CP-06 | Alterno | Completar transacción Nequi con resultado declinado |
| CP-07 | Positivo | Consultar una transacción existente por ID |
| CP-08 | Negativo | Utilizar una credencial privada inválida |

Los escenarios están escritos en español en `src/test/resources/features/nequi/`. El listado completo, incluidos los que se diseñaron pero no se automatizaron, está en el [REVIEW.md](REVIEW.md).

---

## Estructura del proyecto

```
├── README.md            → este archivo
├── REVIEW.md            → documento técnico de revisión
├── .env.example         → plantilla de variables de entorno
├── build.gradle         → dependencias y configuración de la ejecución
└── src/
    ├── main/java/co/wompi/automation/
    │   ├── abilities/      → lo que el actor puede hacer (usar sus llaves)
    │   ├── config/         → lectura de las variables de entorno
    │   ├── interactions/   → llamadas HTTP al API de Wompi
    │   ├── models/         → datos que se envían al API
    │   ├── questions/      → lo que se verifica en las respuestas
    │   ├── tasks/          → acciones de negocio (crear, esperar, consultar)
    │   └── utils/          → firma de integridad y datos de prueba
    └── test/
        ├── java/co/wompi/automation/
        │   ├── hooks/            → preparación antes de cada escenario
        │   ├── runners/          → punto de entrada de las pruebas
        │   └── stepdefinitions/  → conexión entre los escenarios y el código
        └── resources/features/   → escenarios en Gherkin
```

---

## ¿Algo no funcionó?

| Lo que ves | Qué hacer |
|---|---|
| `Falta la variable de entorno obligatoria 'WOMPI_...'` | No creaste el `.env` o le falta esa variable. Revisa el paso 2. |
| Los escenarios fallan con `401` / `Llave no válida` | Alguna llave del `.env` está mal copiada. Verifica que no tenga espacios ni comillas. |
| `La transacción ... siguió en PENDING después de 30 segundos` | El sandbox está respondiendo lento. Aumenta `WOMPI_POLLING_TIMEOUT_SECONDS` en el `.env`. |
| `ConnectTimeoutException` o `SocketTimeoutException` | No hubo conexión con el sandbox a tiempo. Revisa tu internet o aumenta `WOMPI_HTTP_TIMEOUT_SECONDS`. |
| `ConnectTimeoutException` aunque el sandbox sí abre desde el navegador o con `curl` | Algunas redes solo llegan a Wompi por IPv6, y Java usa IPv4 por defecto. Ejecuta agregando `-Djava.net.preferIPv6Addresses=true`, por ejemplo: `./gradlew clean test -Djava.net.preferIPv6Addresses=true` |
| `./gradlew: Permission denied` (macOS / Linux) | Dale permisos de ejecución: `chmod +x gradlew` |
| Error de versión de Java | Verifica con `java -version` que tengas Java 21 o superior. |

# Revel Playwright con Java

Proyecto de ejemplo para practicar **Java**, **Playwright** y automatización web con **Cucumber/Gherkin**. El repositorio usa Java y Maven; no requiere Node.js ni TypeScript.

## Empezar

### 1. Instalar herramientas

- JDK 17 o posterior.
- Maven 3.9 o posterior.
- Visual Studio Code con **Extension Pack for Java** (opcional, recomendado).

En Windows, instala un JDK (por ejemplo, Eclipse Temurin 17) y Maven; asegúrate de que ambos estén disponibles en `PATH`. Cierra y vuelve a abrir VS Code después de instalarlos para que detecte las nuevas variables de entorno.

Comprueba la instalación en una terminal:

```powershell
java -version
mvn -version
```

Si alguno de estos comandos no se reconoce, revisa la instalación y el `PATH` antes de continuar.

### 2. Descargar el navegador de Playwright

Desde la carpeta del proyecto, instala Chromium la primera vez:

```powershell
mvn exec:java "-Dexec.mainClass=com.microsoft.playwright.CLI" "-Dexec.args=install chromium" "-Dexec.classpathScope=test"
```

`CHROMIUM` es el navegador predeterminado. Si eliges otro con `BROWSER`, instala también su binario:

```powershell
# Para BROWSER=FIREFOX
mvn exec:java "-Dexec.mainClass=com.microsoft.playwright.CLI" "-Dexec.args=install firefox" "-Dexec.classpathScope=test"

# Para BROWSER=WEBKIT
mvn exec:java "-Dexec.mainClass=com.microsoft.playwright.CLI" "-Dexec.args=install webkit" "-Dexec.classpathScope=test"
```

### 3. Comprobar la configuración y ejecutar

Primero puedes comprobar que Java compila y que Cucumber encuentra los escenarios y sus pasos:

```powershell
mvn verify "-Dcucumber.execution.dry-run=true"
```

El dry-run no ejecuta los pasos Gherkin ni navega a Revel. Los hooks de Cucumber sí se ejecutan, así que se inicia y cierra una sesión del navegador.

Para ejecutar las pruebas reales:

```powershell
mvn verify
```

Las pruebas abren Revel y dependen de que el sitio esté disponible y de que sus páginas y datos de vehículos no hayan cambiado. No hay escenarios BDD de login.

### Ejecutar desde VS Code

También puedes ejecutar las pruebas sin escribir comandos Maven:

1. Abre **Terminal → Run Task** en el menú de VS Code, o la paleta con `Ctrl+Shift+P` y selecciona **Tasks: Run Task**.
2. Elige una tarea `Playwright: ...`, por ejemplo `Playwright: ejecutar todos los filtros` o una tarea para un escenario concreto.

Las tareas se definen en `.vscode/tasks.json`. La lista incluye la ejecución completa, cada escenario individual, la comprobación dry-run y la instalación de los navegadores. El dry-run no navega a Revel, pero sí ejecuta los hooks que abren y cierran el navegador.

## Qué contiene el proyecto

Las features describen el comportamiento esperado en Gherkin; Cucumber relaciona cada paso con código Java, y Playwright maneja el navegador.

```text
src/test/java/com/revel/playwright/
  config/       Configuración del entorno y del navegador
  locators/     Selectores de elementos de la página
  pages/        Page Objects y acciones reutilizables
  steps/        Pasos Gherkin y ciclo de vida del navegador
  support/      Utilidades compartidas del navegador
  RunCucumberTest.java
src/test/resources/features/
  filtersOurCars.feature
pom.xml
```

El patrón principal es:

```text
Feature (Gherkin) → Step (Java) → Page Object → Locator → Playwright
```

Para seguir el flujo, empieza por `filtersOurCars.feature`, busca la frase de uno de sus pasos en `CarsSteps.java` y continúa hacia `CarsPage.java` y `CarsLocators.java`.

## Añadir una prueba paso a paso

1. Describe el comportamiento en Gherkin dentro de `src/test/resources/features/filtersOurCars.feature`.
2. Implementa los pasos nuevos en `CarsSteps.java`, usando las anotaciones `@Given`, `@When` y `@Then`.
3. Si necesitas interactuar con la página, crea una acción en `CarsPage.java` y su selector en `CarsLocators.java`.
4. Ejecuta el dry-run para comprobar que Cucumber reconoce todos los pasos; después ejecuta la prueba real.

## Ejecutar escenarios concretos

La feature completa tiene la etiqueta `@cars`. Cada caso de prueba tiene además su propio tag, así puedes ejecutar un ejemplo individual:

```powershell
# Ejecutar todos los escenarios de filtros
mvn verify "-Dcucumber.filter.tags=@cars"

# Ejecutar solo un caso: Fuel / Electric Hybrid
mvn verify "-Dcucumber.filter.tags=@filter-fuel-electric-hybrid"

# Ejecutar solo un caso: Body Type / SUV
mvn verify "-Dcucumber.filter.tags=@filter-body-type-suv"

# Ejecutar solo un caso: combinación de filtros
mvn verify "-Dcucumber.filter.tags=@filter-combined-filters"

# Ejecutar solo un caso: filtros sin resultados
mvn verify "-Dcucumber.filter.tags=@filter-no-results"

# Ejecutar solo un caso: limpiar filtros
mvn verify "-Dcucumber.filter.tags=@filter-reset"
```

Los tags `@filter-fuel-electric-hybrid` y `@filter-body-type-suv` están en sus respectivos bloques `Examples`: aunque pertenecen al mismo `Scenario Outline`, seleccionan cada ejemplo por separado.

El único informe HTML del proyecto es **`target/cucumber-reports/cucumber-html-reports/overview-features.html`**. Se genera al ejecutar cualquiera de las tareas de pruebas de VS Code (todas ejecutan `mvn verify`) o el comando Maven equivalente. Abre el índice y selecciona la feature para ver cada ejemplo como un caso individual con los valores del `Scenario Outline` sustituidos.

El informe se genera también si falla un escenario; Maven informa el fallo al final del paso de generación, así se conservan tanto el resultado fallido como su reporte. Cucumber genera `target/cucumber-reports/cucumber.json` como archivo de datos de entrada para crear el HTML; no es un informe que tengas que abrir.

## Primera ejecución en un equipo nuevo

Desde la carpeta raíz del proyecto:

1. Comprueba que `java -version` y `mvn -version` funcionan.
2. Abre **Terminal → Run Task** (o **Tasks: Run Task** desde `Ctrl+Shift+P`) y ejecuta `Playwright: instalar Chromium`.
3. Ejecuta `Playwright: validar steps (dry-run)` para verificar que Cucumber encuentra los pasos.
4. Elige `Playwright: ejecutar todos los filtros` o una tarea para un solo escenario.
5. Al terminar, abre `target/cucumber-reports/cucumber-html-reports/overview-features.html` para consultar el índice del informe.

La primera tarea de Maven puede tardar más porque descarga dependencias; la instalación de Chromium también descarga el navegador. El dry-run no navega a Revel, pero sí inicia el navegador mediante los hooks.

## Solución rápida de problemas

- **`mvn` o `java` no se reconoce:** instala Maven o un JDK 17+, comprueba que estén en `PATH` y reinicia VS Code.
- **Playwright indica que no encuentra el ejecutable del navegador:** ejecuta de nuevo la tarea `Playwright: instalar Chromium` (o instala el navegador que hayas seleccionado en `BROWSER`).
- **Fallan las pruebas después de abrir el navegador:** comprueba la conexión y el sitio; la página, los selectores o los vehículos esperados podrían haber cambiado desde que se escribieron las pruebas.
- **El dry-run falla al iniciar el navegador:** instala primero Chromium. Aunque los pasos Gherkin no se ejecutan, el hook de Cucumber sí inicia el navegador.

## Configuración

La configuración opcional se lee de variables de entorno. No hace falta crear un fichero `.env` ni configurar credenciales para los escenarios actuales.

| Variable | Valor por defecto | Valores |
| --- | --- | --- |
| `ENVIRONMENT` | `QA` | `QA`, `DEV`, `STAGE`, `PROD` |
| `BASE_URL` | `https://driverevel.com` | URL base alternativa |
| `BROWSER` | `CHROMIUM` | `CHROMIUM`, `FIREFOX`, `WEBKIT` |
| `HEADLESS` | `true` | `true`, `false` |

`ENVIRONMENT` es una etiqueta de configuración: actualmente `QA`, `DEV`, `STAGE` y `PROD` usan la misma URL predeterminada, `https://driverevel.com`. Para elegir otro destino, configura `BASE_URL`; esta variable prevalece sobre `ENVIRONMENT`.

Ejemplo para esta terminal de PowerShell:

```powershell
$env:BROWSER = "FIREFOX"
$env:HEADLESS = "false"
mvn verify
```

Para conocer otros detalles del proyecto, consulta [Project Configuration.md](./Project%20Configuration.md).

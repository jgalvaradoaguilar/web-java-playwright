# Configuración del proyecto

## Herramientas y versiones

- Java: JDK 17 o posterior. Maven compila el código con `maven.compiler.release=17`.
- Maven: 3.9 o posterior.
- Microsoft Playwright for Java: versión declarada en `pom.xml`.
- Cucumber-JVM con JUnit Platform: dependencias y versiones declaradas en `pom.xml`.
- Editor recomendado: Visual Studio Code con Extension Pack for Java.

No se necesita Node.js/npm para compilar, probar ni mantener este proyecto.

## Preparar un equipo nuevo en Windows

1. Instala un JDK 17 o posterior y Maven 3.9 o posterior; comprueba que ambos están disponibles en `PATH`.
2. Abre la raíz del proyecto en VS Code y deja que Extension Pack for Java importe el proyecto Maven.
3. Desde **Terminal → Run Task**, ejecuta `Playwright: instalar Chromium`. La primera ejecución de Maven descarga las dependencias y Playwright descarga el navegador.
4. Ejecuta `Playwright: validar steps (dry-run)` para comprobar el descubrimiento de steps; recuerda que esta tarea inicia y cierra el navegador mediante los hooks.
5. Ejecuta `Playwright: ejecutar todos los filtros` o la tarea del escenario que quieras probar.
6. Revisa el informe en `target/cucumber-reports/cucumber-html-reports/overview-features.html`.

Si `java` o `mvn` no se reconocen, revisa `PATH` y reinicia VS Code. Si Playwright no encuentra Chromium, vuelve a ejecutar la tarea de instalación del navegador.

## Tareas de VS Code

`.vscode/tasks.json` ofrece tareas Maven accesibles desde **Terminal → Run Task** o desde **Tasks: Run Task** en la paleta de comandos (`Ctrl+Shift+P`). Incluye una tarea dry-run, la feature completa, una tarea por cada tag de escenario y tareas para instalar Chromium, Firefox o WebKit. Así se pueden lanzar las pruebas desde VS Code sin escribir el comando Maven.

## Organización del código

- `src/test/resources/features/`: escenarios Gherkin.
- `src/test/java/com/revel/playwright/steps/`: definiciones de pasos y hooks Cucumber.
- `src/test/java/com/revel/playwright/pages/`: Page Objects con acciones de alto nivel.
- `src/test/java/com/revel/playwright/locators/`: selectores centralizados.
- `src/test/java/com/revel/playwright/config/`: selección de URL, navegador y entorno.
- `src/test/java/com/revel/playwright/support/`: ciclo de vida de Playwright por escenario.
- `RunCucumberTest.java`: suite JUnit Platform que descubre las features.

Flujo de una prueba: `Feature → Step → Page Object → Locator → Playwright`.

## Comandos de desarrollo

```powershell
# Compilar el código de prueba
mvn test-compile

# Validar el descubrimiento de escenarios y pasos (los hooks inician el navegador, los pasos no navegan)
mvn verify "-Dcucumber.execution.dry-run=true"

# Ejecutar pruebas contra el sitio
mvn verify

# Ejecutar solo el conjunto etiquetado @cars
mvn verify "-Dcucumber.filter.tags=@cars"

# Ejecutar solo el escenario de filtro eléctrico híbrido
mvn verify "-Dcucumber.filter.tags=@filter-fuel-electric-hybrid"

# Ejecutar solo el escenario de SUV
mvn verify "-Dcucumber.filter.tags=@filter-body-type-suv"

# Ejecutar solo la combinación de filtros
mvn verify "-Dcucumber.filter.tags=@filter-combined-filters"

# Ejecutar solo el escenario sin resultados
mvn verify "-Dcucumber.filter.tags=@filter-no-results"

# Ejecutar solo la limpieza de filtros
mvn verify "-Dcucumber.filter.tags=@filter-reset"
```

`@cars` está en la feature y cubre todos sus escenarios. Los tags `@filter-fuel-electric-hybrid` y `@filter-body-type-suv` se aplican a bloques `Examples` distintos para poder ejecutar de forma aislada las dos filas del primer `Scenario Outline`.

La primera ejecución descarga las dependencias Maven. Instala Chromium una vez con:

```powershell
mvn exec:java "-Dexec.mainClass=com.microsoft.playwright.CLI" "-Dexec.args=install chromium" "-Dexec.classpathScope=test"
```

El valor de `BROWSER` determina si `BrowserSession` inicia Chromium, Firefox o WebKit. Si cambias de motor, instala el navegador correspondiente con `-Dexec.args=install firefox` o `-Dexec.args=install webkit`.

El único informe HTML es `target/cucumber-reports/cucumber-html-reports/overview-features.html`. `mvn verify` genera ese informe mediante el plugin `maven-cucumber-reporting`; las tareas de pruebas de VS Code también ejecutan `verify`, por lo que generan el mismo informe. Abre el índice y selecciona la feature para ver cada ejemplo como un caso individual con los valores concretos del `Scenario Outline`. Cucumber produce `target/cucumber-reports/cucumber.json` como entrada del generador; no se genera un segundo HTML nativo.

Surefire deja que el generador de informes se ejecute incluso si fallan escenarios; después el plugin comprueba los resultados Cucumber y Maven termina con error si hubo fallos. Los pasos omitidos por un dry-run no cuentan como fallidos.

## Datos y ejecución

Las pruebas actuales cubren filtros de vehículos y navegan al sitio Revel indicado por `BASE_URL` (por defecto `https://driverevel.com`). Por tanto, una ejecución normal requiere conexión a internet y depende de la disponibilidad y contenido actual del sitio.

Configuración opcional mediante variables de entorno:

- `ENVIRONMENT`: `QA`, `DEV`, `STAGE` o `PROD`; por defecto `QA`.
- `BASE_URL`: URL base alternativa.
- `BROWSER`: `CHROMIUM`, `FIREFOX` o `WEBKIT`; por defecto `CHROMIUM`.
- `HEADLESS`: `true` o `false`; por defecto `true`.

`ENVIRONMENT` se valida como etiqueta, pero de momento no asigna URLs distintas: los cuatro valores utilizan `https://driverevel.com`. Para apuntar a otro entorno o instancia, configura `BASE_URL`, que prevalece sobre `ENVIRONMENT`.

No se requieren credenciales para las features actuales. No añadas teléfonos, OTPs ni datos personales al código o a las features.

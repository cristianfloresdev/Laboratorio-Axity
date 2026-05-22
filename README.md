# 🦕 DinoPark Simulation

Simulación secuencial de un parque turístico de dinosaurios desarrollada 
en Java 21 con Maven puro. El sistema modela el comportamiento de turistas, 
trabajadores y dinosaurios en diferentes zonas, aplicando patrones de diseño, 
persistencia con H2 + Liquibase, y eventos aleatorios no deterministas.

---

## 🛠️ Herramientas utilizadas

| Herramienta       | Versión      |
|-------------------|--------------|
| Java              | 21           |
| Maven             | 3.9.x        |
| H2 Database       | 2.2.224      |
| Liquibase         | 4.27.0       |
| JUnit Jupiter     | 5.10.1       |
| Mockito           | 5.8.0        |
| JaCoCo            | 0.8.11       |

---

## ⚙️ Configuración

Toda la configuración del sistema se encuentra en:

```
src/main/resources/park.properties
```

```properties
# Simulación
simulation.totalSteps=100
simulation.arrivalBatchSize=5

# Turistas y dinosaurios
tourists=50
dinosaurs.carnivores=5
dinosaurs.herbivores=15

# Trabajadores
workers.guards=3
workers.technicians=2
workers.dailySalary=150.0

# Zona de arribo
arrival.maxCapacity=30
arrival.ticketPrice=25.0

# Recinto central
hub.souvenirPrice=15.0
hub.souvenirPurchaseProbability=0.4

# Baños
bathroom.maxCapacity=10
bathroom.useDurationSteps=3
bathroom.spaPrice=20.0
bathroom.spaPurchaseProbability=0.2

# Planta de energía
powerplant.initialEnergy=100.0
powerplant.consumptionPerStep=1.5
powerplant.failureProbability=0.05
powerplant.maintenanceCost=200.0
powerplant.repairCost=500.0

# Recintos de observación
enclosure.basic.maxVisitors=20
enclosure.basic.entryFee=10.0
enclosure.premium.maxVisitors=12
enclosure.premium.entryFee=30.0
enclosure.vip.maxVisitors=5
enclosure.vip.entryFee=75.0

# Vehículos
vehicles.count=4
vehicles.repairSteps=5

# Eventos aleatorios
event.escape.probability=0.05
event.blackout.probability=0.03
event.storm.probability=0.04
event.deals.probability=0.08
event.vehicleFailure.probability=0.06

# Monitoreo
monitoring.intervalSteps=10

# Persistencia: csv | db
output.mode=csv
output.directory=output

# Base de datos H2
db.path=./data/parkdb
db.user=sa
db.password=
```

---

## 🚀 Ejecución

**Compilar:**
```bash
mvn compile
```

**Correr pruebas:**
```bash
mvn test
```

**Ejecutar la simulación:**
```bash
mvn exec:java -Dexec.mainClass="com.dinopark.Main"
```

**Ver reporte de cobertura:**
```bash
mvn test
# Abrir en el navegador:
target/site/jacoco/index.html
```

**Cambiar entre CSV y base de datos:**
```properties
# En park.properties
output.mode=csv   # genera archivos en output/
output.mode=db    # guarda en data/parkdb.mv.db
```

---

## 📋 Explicación general del sistema

El sistema simula el funcionamiento de un parque de dinosaurios durante 
N steps de tiempo configurables. En cada step ocurre lo siguiente:

1. **Llegadas** — un batch de turistas entra desde la cola de espera
2. **Movimiento** — los turistas activos visitan zonas (Hub, Baños, Recintos)
3. **Ticks** — las zonas actualizan su estado interno
4. **Eventos aleatorios** — el engine evalúa cada evento por probabilidad
5. **Monitoreo** — cada N steps se imprime el estado del parque

### Zonas del parque

| Zona | Responsabilidad |
|---|---|
| `ArrivalZone` | Cola de entrada, venta de boletos |
| `CentralHub` | Venta de souvenirs con probabilidad configurable |
| `BathroomZone` | Capacidad limitada, tiempo de uso, servicio SPA |
| `ObservationEnclosure` | Experiencias BASIC/PREMIUM/VIP con encuestas |
| `PowerPlant` | Suministro de energía, fallas aleatorias |

### Eventos aleatorios

| Evento | Efecto |
|---|---|
| `DinosaurEscapeEvent` | Un dinosaurio escapa, puede atacar turista |
| `BlackoutEvent` | Apaga la planta eléctrica |
| `StormEvent` | Evacúa a todos los turistas activos |
| `DealsHourEvent` | Activa 30% de descuento en ese step |
| `VehicleFailureEvent` | Rompe un vehículo disponible |

### Sistema de monitoreo

Cada `monitoring.intervalSteps` steps se imprimen 5 métricas:
╔══════════ PARK MONITOR ══════════╗

Active tourists     : 49
Dinos in enclosure  : 18
Power plant energy  : 80.0 / 100.0
Active events       : none
Vehicles unavailable: 0
╚══════════════════════════════════╝


---

## 🎨 Patrones de diseño

### 1. Singleton — `ParkConfig`

**Descripción:** Garantiza que la configuración del parque tenga exactamente 
una instancia en toda la aplicación. El constructor es `private` y el acceso 
se realiza exclusivamente mediante `getInstance()`.

**Justificación:** `park.properties` debe leerse una sola vez al arrancar. 
Si cada clase creara su propio `ParkConfig`, se abriría el archivo múltiples 
veces con riesgo de configuraciones inconsistentes.

```java
// Constructor privado — nadie puede hacer new ParkConfig()
private ParkConfig() {
    props = new Properties();
    // carga park.properties una sola vez
}

// Único punto de acceso global
public static ParkConfig getInstance() {
    if (instance == null) instance = new ParkConfig();
    return instance;
}

// Uso en cualquier clase
int tourists = ParkConfig.getInstance().getInt("tourists", 50);
```

<img width="751" height="452" alt="image" src="https://github.com/user-attachments/assets/6a5c3db0-eee8-4665-8bb3-d0d5189c1396" />


---

### 2. Strategy — `ParkDataWriter`

**Descripción:** La interfaz `ParkDataWriter` define el contrato de 
persistencia. `CsvWriter` y `DatabaseService` son implementaciones 
intercambiables que se eligen según `output.mode` en `park.properties`.

**Justificación:** Permite cambiar el mecanismo de persistencia de CSV 
a base de datos H2 sin modificar ninguna zona ni el engine. El código 
trabaja contra la interfaz, no contra una implementación concreta.

```java
// La interfaz — contrato común
public interface ParkDataWriter {
    void appendRevenue(RevenueRecord record);
    void appendExpense(ExpenseRecord record);
    void appendEvent(EventRecord record);
    void close();
}

// Selección en tiempo de ejecución
ParkDataWriter db;
if (mode.equals("db")) {
    db = new DatabaseService(path);  // guarda en H2
} else {
    db = new CsvWriter(outputDir);   // guarda en CSV
}
```

<img width="656" height="539" alt="image" src="https://github.com/user-attachments/assets/705acb99-9a50-42fd-b3f8-b91f65dfb17c" />


---

## 🗂️ Estructura del proyecto
src/main/java/com/dinopark/
├── config/          ← ParkConfig (Singleton)
├── enums/           ← VehicleStatus, TouristStatus, DinosaurStatus...
├── event/           ← SimulationEvent + 5 implementaciones
├── exception/       ← Excepciones personalizadas
├── model/           ← Dinosaur, Tourist, Worker, Vehicle, records
├── monitoring/      ← ParkMonitor
├── persistence/     ← CsvWriter, DatabaseService, ParkDataWriter
├── simulation/      ← SimulationEngine, ParkState
├── zone/            ← ArrivalZone, CentralHub, BathroomZone...
└── Main.java

<img width="569" height="810" alt="image" src="https://github.com/user-attachments/assets/6e127a71-f1ba-46e1-8439-52ea1b61d388" />


<img width="675" height="708" alt="image" src="https://github.com/user-attachments/assets/a30cb3b4-7778-4b3c-bf5f-df107c94f778" />


---

## 📊 Cobertura de pruebas

Cobertura alcanzada: **68%** — mínimo requerido: 65% aunque en las estadisticas dicen que 70%

<img width="1091" height="379" alt="image" src="https://github.com/user-attachments/assets/53dba470-fcea-44a5-8264-2e2ca6d7fde2" />

---

## 📁 Archivos generados

Al ejecutar la simulación se generan automáticamente:
output/
├── revenue.csv    ← ingresos: boletos, souvenirs, SPA, recintos
├── expense.csv    ← gastos operativos
└── events.csv     ← eventos ocurridos durante la simulación
data/
└── parkdb.mv.db   ← base de datos H2 (cuando output.mode=db)

---

## 🧪 Pruebas unitarias

```bash
mvn test
```

| Clase de test | Qué verifica |
|---|---|
| `ParkConfigTest` | Singleton, lectura de propiedades, defaults |
| `TouristTest` | Estado inicial, spend(), recordVisit() |
| `DinosaurTest` | escape(), recapture(), dangerLevel |
| `VehicleTest` | Ciclo de vida: use, free, markBroken, tick |
| `ArrivalZoneTest` | Procesamiento de cola, cobro de boleto |
| `PowerPlantTest` | triggerFailure(), repair(), tick() |
| `ObservationEnclosureTest` | Cobro de entrada, encuestas |
| `DinosaurEscapeEventTest` | escape(), lista vacía sin excepción |
| `BlackoutEventTest` | Planta no operacional tras execute() |
| `DealsHourEventTest` | Descuento 30%, reset tras clearActiveEvents() |
| `VehicleFailureEventTest` | Rompe vehículo AVAILABLE, lista vacía sin excepción |
| `DatabaseServiceTest` | INSERT y SELECT en H2 para las 3 tablas |
| `CsvWriterTest` | Escritura en archivo, creación de CSVs |
| `ParkStateTest` | Métricas, vehículos, eventos activos |
| `SimulationEngineTest` | Corre sin excepción, ingresos > 0 |

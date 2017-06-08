# VanillaBench

VanillaBench is a benchmarking tool for testing the performance (throughput and latency) in extreme environment.

## Type of Benchmarks

- Micro-benchmarks
  - Design (TBA)
- TPC-C (New-Order, Payment)
- TPC-E (TBA)

## How To Benchmark

### Benchmarking using Eclipse

1. Clone this repository:
  ```bash
  > git clone https://github.com/vanilladb/vanillabench.git
  ```

2. Clone [VanillaCore] (Optional, if you want to test your modification on VanillaCore):
  ```bash
  > git clone https://github.com/vanilladb/vanillacore.git
  ```

3. Import the projects into Eclipse by selecting `File` > `Import` > `Maven` > `Existing Maven Projects`.

4. Create run configurations for clients and servers.
  - Server
    - Type: `Java Application`
    - Project: `vanillabench`
    - Main Class: `org.vanilladb.bench.server.StartUp`
    - Program Arguments: `[DB Name]`
    - VM Arguments:
      ```
      -Djava.util.logging.config.file=target/classes/java/util/logging/logging.properties
      -Dorg.vanilladb.bench.config.file=target/classes/org/vanilladb/bench/vanillabench.properties
      -Dorg.vanilladb.core.config.file=target/classes/org/vanilladb/core/vanilladb.properties
      ```
  - Client
    - Type: `Java Application`
    - Project: `vanillabench`
    - Main Class: `org.vanilladb.bench.App`
    - Program Arguments: `[Action]` (1 = Loading Testbed,  2 = Benchmarking)
    - VM Arguments:
      ```
      -Djava.util.logging.config.file=target/classes/java/util/logging/logging.properties
      -Dorg.vanilladb.bench.config.file=target/classes/org/vanilladb/bench/vanillabench.properties
      -Dorg.vanilladb.core.config.file=target/classes/org/vanilladb/core/vanilladb.properties
      ```

5. Modify the properties in `src/main/resources/org/vanilladb/bench/vanillabench.properties` as your requirements.

6. Start up a server using the configuration.

7. Start up a client using the configuration.
  - You may need to load the testing data at least once before benchmarking.

[VanillaCore]: https://github.com/vanilladb/vanillacore

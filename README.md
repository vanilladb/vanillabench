# VanillaBench

[![Build Status](https://travis-ci.org/vanilladb/vanillabench.svg?branch=master)](https://travis-ci.org/vanilladb/vanillabench)
[![Apache 2.0 License](https://img.shields.io/badge/license-apache%202.0-orange.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![Maven Central](https://img.shields.io/maven-central/v/org.vanilladb/bench.svg)](https://maven-badges.herokuapp.com/maven-central/org.vanilladb/bench)

VanillaBench is a benchmarking tool for testing the performance (throughput and latency) in extreme environment.

## Type of Benchmarks

- Micro-benchmarks
  - Design (TBA)
- TPC-C (New-Order, Payment)
- TPC-E (Simplified Trade-Order, Trade-Result)
- Yahoo Cloud Benchmarks (YCSB)

## Getting Started

This tutorial will guide you loading the testing data and running a benchmarking job.

### Using Maven

The project is a Maven project, which means you can do all the work for running this project using Maven.

1. Clone this repository:
  ```bash
  > git clone https://github.com/vanilladb/vanillabench.git
  ```

2. Execute the following command in the root of the project directory:
  ```bash
  > mvn clean package
  ```
  This will make Maven compile, test, package a jar and copy all the dependencies it needs to `target/stand-alone`.

3. Change the working directory to `target/stand-alone`. Check if all the configurations in the `properties`
  are the same as your demands.

4. To start up a [VanillaCore] server for benchmarking, execute the following command:

  For Unix-like systems:
  ```bash
  > bin/server {DB Name}
  ```
  For Windows:
  ```bash
  > bin/server.bat {DB Name}
  ```

  (Please replace `{DB Name}` with your database name, which will be the name of the folder of your database files)

5. To start up a benchmarking client, execute the following command:

  For Unix-like systems:
  ```bash
  > bin/client {Action}
  ```
  For Windows:
  ```bash
  > bin/client.bat {Action}
  ```

  The `{Action}` you can use are:

  - `1` - Loading testing data
  - `2` - Running benchmarking RTEs

  For example, if I want to load the testing data:
  ```bash
  > bin/client 1
  ```

### Using Eclipse

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

## Linking via Maven

```xml
<dependency>
  <groupId>org.vanilladb</groupId>
  <artifactId>bench</artifactId>
  <version>0.4.0</version>
</dependency>
```

## Contact Information

If you have any question, you can either open an issue here or contact [vanilladb@datalab.cs.nthu.edu.tw](vanilladb@datalab.cs.nthu.edu.tw) directly.

## License

Copyright 2016-2020 vanilladb.org contributors

Licensed under the [Apache License 2.0](LICENSE)

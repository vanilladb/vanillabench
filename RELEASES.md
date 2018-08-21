# Version 0.3.0 (2018-08-21)

## Enhancements

- Added JDBC connection utility tools ([#20])
- Added JDBC support for micro-benchmarks ([#20])
- Added benchmark name at the end of the filename of reports ([#20])

## Code Refactoring

- Moved benchmark-spcecific classes to `org.vanilladb.bench.benchmarks` ([#20])
- Refactored `StatisticMgr` for readability ([#20])
- Made `RemoteTerminalEmulator`, `TransactionExecutor` and `TxParamGenerator` generic to `TransactionType` ([#20])
- Merged `SchemaBuilder` into `TestbedLoader` ([#20])

## Bug Fixes

- Fixed the bug casuing RuntimeException during updating ([#15])

[#15]: https://github.com/vanilladb/vanillabench/pull/15
[#20]: https://github.com/vanilladb/vanillabench/pull/20

# Version 0.2.1 (2017-07-04)

## Refactoring

- Added the benchmark name to the names of classes `SchemaBuilder` and `TestbedLoader` ([#12])
- Maked the naming in stored procedures consistent ([#12])
- Maked `TpceDataManager` accept scaling parameters to adopt to ElasqlBench ([#12])

## Bug Fixes

- Fixed a bug that makes the loader cannot find the TPC-E data ([#12])

[#12]: https://github.com/vanilladb/vanillabench/pull/12

# Version 0.2.0 (2017-07-03)

## Refactoring

- Moved all RTE packages to their corresponding workloads ([#6])
- Moved `executeQuery` and `executeUpdate` to `BasicStoredProcedure` ([#8])

## Enhancements

- Added a simplified version of TPC-E ([#9])
	- The simplified version contains Trade-Order transactions and Trade-Result transactions
	- The Trade-Order transactions and Trade-Result transactions implemented in this version are also simplified

## Bug Fixes

- Avoided scientific notation in TPC-C payment ([#7])

[#6]: https://github.com/vanilladb/vanillabench/pull/6
[#7]: https://github.com/vanilladb/vanillabench/pull/7
[#8]: https://github.com/vanilladb/vanillabench/pull/8
[#9]: https://github.com/vanilladb/vanillabench/pull/9

# Version 0.1.0 (2017-06-10)

- The core functions are ready.
- The Micro-benchmarks
  - All it needs are implemented.
- TPC-C
  - New-order transactions and Payment transactions are implemented.
- TPC-E
  - Nothing is implemented.
- JDBC
  - Wait for implementation.

# Release Notes

## Version 0.4.2 (2021-09-02)

### Bug Fixes

- Fix the problem that `RandomValueGenerator` generates value even when the probability is 0.

[#37]: https://github.com/vanilladb/vanillabench/pull/37

## Version 0.4.1 (2021-06-13)

### Enhancements

- Add a thread to compress the resulted report in order to minimize memory footprint ([#27], [#31])
- Update the version of dependencies (VanillaCore and JUnit) ([#32])

### Refactor

- Refactor `StatisticsMgr` ([#28])

### Bug Fixes

- Fix the problem that TpccCheckingProc creates too many temp files ([#26])
- Fix a type in `VanillaBench` ([#30])
- Exclude aborted transactions from the report ([#33])

[#26]: https://github.com/vanilladb/vanillabench/pull/26
[#27]: https://github.com/vanilladb/vanillabench/pull/27
[#28]: https://github.com/vanilladb/vanillabench/pull/28
[#30]: https://github.com/vanilladb/vanillabench/pull/30
[#31]: https://github.com/vanilladb/vanillabench/pull/31
[#32]: https://github.com/vanilladb/vanillabench/pull/32
[#33]: https://github.com/vanilladb/vanillabench/pull/33

## Version 0.4.0 (2020-02-25)

All the following changes were merged in [#23].

### Enhancements

- Add the YCSB benchmarks
- Make the result of a transaction lazily evaluated
- Add a method to check if a transaction is benchmarking transaction in `BenchTransactionType`
- Add check procedures that check database before benchmarking for all the benchmarks

### Refactor

- Update the implementation according to the [changes](https://github.com/vanilladb/vanillacore/pull/44) of of VanillaCore `0.4.0`
- Make `TxnResultSet` immutable
- Rename `MicrobenchmarkTxnType` to `MicrobenchTransactionType`
- Separate profiling transaction from benchmark transaction types
- Add a note (FIXME) in `PaymentParamGen`
- Remove an unused variable in `PaymentProcParamHelper`
- Add `VanillaBench` as the main controller
- Use `Benchmark` instead of `Benchmarker`
- Change the way that `StatisticMgr` records time (more accurately)
- Simplify the action of `StoredProcedureHelper`

### Bug Fixes

- Consider aborts in reports
- Fix some typos
- Add missing arguments in `MicroBenchmarker`
- Add a check for printing results in `SutResultSet`
- Correct the properties files
- Fix a bug that causes the aborted `Payment` transactions are missing
- Make `BasicStoredProcFactory` work
- Fix the output reports

[#23]: https://github.com/vanilladb/vanillabench/pull/23

## Version 0.3.0 (2018-08-21)

### Enhancements

- Added JDBC connection utility tools ([#20])
- Added JDBC support for micro-benchmarks ([#20])
- Added benchmark name at the end of the filename of reports ([#20])

### Code Refactoring

- Moved benchmark-spcecific classes to `org.vanilladb.bench.benchmarks` ([#20])
- Refactored `StatisticMgr` for readability ([#20])
- Made `RemoteTerminalEmulator`, `TransactionExecutor` and `TxParamGenerator` generic to `TransactionType` ([#20])
- Merged `SchemaBuilder` into `TestbedLoader` ([#20])

### Bug Fixes

- Fixed the bug casuing RuntimeException during updating ([#15])

[#15]: https://github.com/vanilladb/vanillabench/pull/15
[#20]: https://github.com/vanilladb/vanillabench/pull/20

## Version 0.2.1 (2017-07-04)

### Refactoring

- Added the benchmark name to the names of classes `SchemaBuilder` and `TestbedLoader` ([#12])
- Maked the naming in stored procedures consistent ([#12])
- Maked `TpceDataManager` accept scaling parameters to adopt to ElasqlBench ([#12])

### Bug Fixes

- Fixed a bug that makes the loader cannot find the TPC-E data ([#12])

[#12]: https://github.com/vanilladb/vanillabench/pull/12

## Version 0.2.0 (2017-07-03)

### Refactoring

- Moved all RTE packages to their corresponding workloads ([#6])
- Moved `executeQuery` and `executeUpdate` to `BasicStoredProcedure` ([#8])

### Enhancements

- Added a simplified version of TPC-E ([#9])
	- The simplified version contains Trade-Order transactions and Trade-Result transactions
	- The Trade-Order transactions and Trade-Result transactions implemented in this version are also simplified

### Bug Fixes

- Avoided scientific notation in TPC-C payment ([#7])

[#6]: https://github.com/vanilladb/vanillabench/pull/6
[#7]: https://github.com/vanilladb/vanillabench/pull/7
[#8]: https://github.com/vanilladb/vanillabench/pull/8
[#9]: https://github.com/vanilladb/vanillabench/pull/9

## Version 0.1.0 (2017-06-10)

- The core functions are ready.
- The Micro-benchmarks
  - All it needs are implemented.
- TPC-C
  - New-order transactions and Payment transactions are implemented.
- TPC-E
  - Nothing is implemented.
- JDBC
  - Wait for implementation.

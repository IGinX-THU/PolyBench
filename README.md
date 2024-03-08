# PolyBench

这个资源库提供了示例应用程序和进一步的基准测试工具，用于评估和开始使用 IGinX。

## Gavel

### 数据
- user (id, email, password, last_name, first_name, gender, birthday, country, city, zip_code) => PostgreSQL
- category (id, name) => PostgreSQL
- auction (id, title, description, start_date, end_date, category, user) => MongoDB
- picture (filename, type, size, auction) => PostgreSQL
- bid (id, amount, timestamp, user, auction) => IoTDB

### 测试方法

[Gavel说明文档](gavel/README.md)

## 数据清洗

数据清洗的目标为找出这些破坏了Functional Dependencies的重复的 zipcode 👉 state 映射

### 数据
一张表，包含两列，分别为zipcode和state，均为int类型

### 测试方法

1. 通过python脚本生成csv
    ```shell
    python3 dataCleaning/gen_data.py -n xxx # -n 后面接行数？
    ```
2. 导入Postgres
   TODO
3. 查询方法
   [数据清洗说明文档](dataCleaning/README.md)

## TPC-H

### 数据
tpch benchmark

### 测试方法
1. 通过tpch数据生成工具生成测试数据
   ```shell
   ./dbgen -s 1 -f # 这里-s后指定数据集大小为几G
   ```
2. 分别导入xx、Postgres、Parquet数据库
3. 查询方法
   TODO sql语句

## SGD

TODO

## Pagerank

TODO
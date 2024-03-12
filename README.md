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
一张表，包含三列，分别为key,zipcode和state，均为int类型

### 测试方法
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

### 数据

[数据来源](https://archive.ics.uci.edu/dataset/280/higgs)

一张表，共29列，其中第一列是标签label， 之后是特征。
前 21 个特征（第 2-22 列）是加速器中粒子探测器测量到的运动特性。最后七个特征是前 21 个特征的函数，均为float类型

数据量大，有1100万行

### 测试方法

1. 从网站上下载并处理数据
   ```shell
   cd SGD
   ./getData.sh
   # 如果想只保留前x行并写入HIGGS2.csv，可以执行：
   cat HIGGS.csv | head -n x > HIGGS2.csv
   ```
2. 导入Postgres数据库

   ```postgresql
   CREATE TABLE higgstrainall (
    label INT,
    lepton_pt FLOAT,
    lepton_eta FLOAT,
    lepton_phi FLOAT,
    missing_energy_magnitude FLOAT,
    missing_energy_phi FLOAT,
    jet1_pt FLOAT,
    jet1_eta FLOAT,
    jet1_phi FLOAT,
    jet1_b_tag FLOAT,
    jet2_pt FLOAT,
    jet2_eta FLOAT,
    jet2_phi FLOAT,
    jet2_b_tag FLOAT,
    jet3_pt FLOAT,
    jet3_eta FLOAT,
    jet3_phi FLOAT,
    jet3_b_tag FLOAT,
    jet4_pt FLOAT,
    jet4_eta FLOAT,
    jet4_phi FLOAT,
    jet4_b_tag FLOAT,
    m_jj FLOAT,
    m_jjj FLOAT,
    m_lv FLOAT,
    m_jlv FLOAT,
    m_bb FLOAT,
    m_wbb FLOAT,
    m_wwbb FLOAT
   );
   \copy "higgstrainall"   from '/Users/janet/Desktop/Polystore-utils/SGD/HIGGS.csv'    DELIMITER ',' CSV;
   ```

3. 查询方法

   以刚导入数据的Postgres引擎启动IGinX，将`IGinX/conf/config.properties`配置为

   ```
   storageEngineList=127.0.0.1#5432#postgresql#username=postgres#password=postgres#has_data=true
   ```

   在iginx中执行如下sql语句：
   ```sql
   REGISTER UDAF PYTHON TASK "UDAFtrainall" IN "/Users/janet/Desktop/Polystore-utils/SGD/udaf_trainall.py" AS "trainall";
   select  trainall(key,label,lepton_pt,lepton_eta,lepton_phi,missing_energy_magnitude,missing_energy_phi,jet1_pt,jet1_eta,jet1_phi,jet1_b_tag,jet2_pt,jet2_eta,jet2_phi,jet2_b_tag,jet3_pt,jet3_eta,jet3_phi,jet3_b_tag,jet4_pt,jet4_eta,jet4_phi,jet4_b_tag,m_jj,m_jjj,m_lv,m_jlv,m_bb,m_wbb,m_wwbb, 1) from mongotpch.higgstrainall;
   ```

## Pagerank

### 数据
[数据来源](https://snap.stanford.edu/data/web-Google.html)

一张表，包含两列，分别为fromnode和tonode，代表有向边的起始点和终点，均为int类型。

### 测试方法
1. 从网站上下载并处理数据
   ```shell
   ./getData.sh
   ```
2. 导入Postgres数据库

   ```postgresql
   CREATE TABLE IF NOT EXISTS pagerankall (
      fromnode INT,
      tonode INT
   );
   \copy pagerankall from '/Users/janet/Desktop/Polystore-utils/Pagerank/web-google.csv'  DELIMITER ',' CSV HEADER;
   ```
   如果`\copy`导入时报错需要执行：
   ```shell
   dos2unix web-google.csv
   ```

3. 查询方法

   以刚导入数据的Postgres引擎启动IGinX，将`IGinX/conf/config.properties`配置为

   ```
   storageEngineList=127.0.0.1#5432#postgresql#username=postgres#password=postgres#has_data=true
   ```
   
   在iginx中执行如下sql语句：
   ```sql
   REGISTER UDSF PYTHON TASK "UDSFpagerankall" IN  "/Users/janet/Desktop/Polystore-utils/Pagerank/udsf_pagerankall.py" AS "pagerankall";
   select * from (select pagerankall(key, fromnode, tonode) as pagerankall from postgres.pagerankall) order by pagerankall desc limit 100;
   ```
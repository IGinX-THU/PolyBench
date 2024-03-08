# Gavel 说明文档
1. 生成数据

   执行`GenerateData.java`中的`main()`函数。

2. 导入IoTDB：

   使用IoTDB 0.12.6版本。

   启动IoTDB server后，进入`apache-iotdb-0.12.6-server-bin/tools`文件夹，执行：

   ```shell
   ./import-csv.sh -h 127.0.0.1 -p 6667 -u root -pw root -f /path/to/Polystore-utils/gavel/generatedData/bid.csv
   ```

3. 导入Postgres：
   1. 创建数据库gavel并赋权

      ```postgresql
      drop database gavel;
      create database gavel;
      GRANT ALL PRIVILEGES ON DATABASE gavel TO postgres;
      \c gavel
      ```

    2. 创建各表并赋权
       ```postgresql
       drop table if exists "user";
       drop table if exists picture;
       drop table if exists category;
       
       create table "user" (
       id              integer,
       email           varchar(64),
       password        varchar(16),
       last_name       varchar(16),
       first_name      varchar(16),
       gender          varchar(6),
       birthday        varchar(16),
       country         varchar(64),
       city            varchar(32),
       zip_code        varchar(16),
       primary key (id)
       );
       
       create table category (
       id              integer,
       name            varchar(16),
       primary key (id)
       );
       
       create table picture (
       filename        varchar(64),
       type            integer,
       size            bigint,
       auction         integer,
       primary key (filename)
       );
       
       GRANT ALL ON "user" TO postgres;
       GRANT ALL ON category TO postgres;
       GRANT ALL ON picture TO postgres;
       ```
   3. 导入CSV

      ```postgresql
      COPY "user"
      FROM '/path/to/Polystore-utils/gavel/generatedData/user.csv'
      DELIMITER ','
      CSV HEADER;
      
      COPY category
      FROM '/path/to/Polystore-utils/gavel/generatedData/category.csv'
      DELIMITER ','
      CSV HEADER;
      
      COPY picture
      FROM '/path/to/Polystore-utils/gavel/generatedData/picture.csv'
      DELIMITER ','
      CSV HEADER;
      ```

4. 启动IGinX	

    - 以一个不带数据的MongoDB启动IGinX，将`IGinX/conf/config.properties`配置为

      ```
      storageEngineList=127.0.0.1#27017 #mongodb
      ```

5. 将数据导入IGinX

    进入`IGinX/tools-exportCsv/target/iginx-tools-expCsv-0.6.0-SNAPSHOT/sbin`文件夹。

    执行：

    ```shell
    ./import_csv.sh -f /path/to/Polystore-utils/gavel/generatedData/auction.csv
    ```

6. IGinX添加存储节点

   ```
   ADD STORAGEENGINE ("127.0.0.1", 5432, "postgresql", "has_data:true, username:postgres, password:postgres, sessionPoolSize: 20");
   ADD STORAGEENGINE ("127.0.0.1", 6667, "iotdb12", "username:root, password:root, sessionPoolSize:20, has_data:true");
   ```

   添加后执行 `show columns;` 结果如下：

   ```
   +-----------------------------------------------+--------+
   |                                           Path|DataType|
   +-----------------------------------------------+--------+
   |                         gavel.auction.category| INTEGER|
   |                      gavel.auction.description|  BINARY|
   |                         gavel.auction.end_date|  BINARY|
   |                               gavel.auction.id| INTEGER|
   |                       gavel.auction.start_date|  BINARY|
   |                            gavel.auction.title|  BINARY|
   |                             gavel.auction.user| INTEGER|
   |                               gavel.bid.amount| INTEGER|
   |                              gavel.bid.auction| INTEGER|
   |                                   gavel.bid.id| INTEGER|
   |                                 gavel.bid.user| INTEGER|
   |                              gavel.category.id| INTEGER|
   |                            gavel.category.name|  BINARY|
   |                          gavel.picture.auction| INTEGER|
   |                         gavel.picture.filename|  BINARY|
   |                             gavel.picture.size|    LONG|
   |                             gavel.picture.type| INTEGER|
   |                            gavel.user.birthday|  BINARY|
   |                                gavel.user.city|  BINARY|
   |                             gavel.user.country|  BINARY|
   |                               gavel.user.email|  BINARY|
   |                          gavel.user.first_name|  BINARY|
   |                              gavel.user.gender|  BINARY|
   |                                  gavel.user.id| INTEGER|
   |                           gavel.user.last_name|  BINARY|
   |                            gavel.user.password|  BINARY|
   |                            gavel.user.zip_code|  BINARY|
   +-----------------------------------------------+--------+
   ```

7. 查询方法

   执行sql，这里需要修改where子句中`gavel.user.password = "ceJWN9GynL"`的条件，可以直接修改为执行 `select password from gavel.user limit 1` 的结果：

   ```sql
   select * from gavel.user join gavel.bid on gavel.user.id = gavel.bid.user join gavel.auction on gavel.bid.user = gavel.auction.user where gavel.user.password = "ceJWN9GynL";
   select gavel.bid.amount from gavel.user join gavel.bid on gavel.user.id = gavel.bid.user join gavel.auction on gavel.bid.user = gavel.auction.user where gavel.user.password = "ceJWN9GynL";
   select gavel.bid.amount from gavel.user join gavel.bid on gavel.user.id = gavel.bid.user join gavel.auction on gavel.bid.user = gavel.auction.user where gavel.user.password = "ceJWN9GynL" and gavel.bid.amount < 100000;
   select avg(gavel.bid.amount) from gavel.user join gavel.bid on gavel.user.id = gavel.bid.user join gavel.auction on gavel.bid.user = gavel.auction.user where gavel.user.password = "ceJWN9GynL" and gavel.bid.amount < 100000;
   ```

   

# 数据清洗说明文档

1. 通过python脚本生成csv
    ```shell
    python3 dataCleaning/gen_data.py -n xxx # -n 后面接行数
    ```
2. 导入iginx
   指定存储引擎为parquet，之后启动iginx执行：
   
    ```
   LOAD DATA FROM INFILE "/path/to/Polystore-utils/dataCleaning/zipcode_city.csv" AS CSV INTO uszip(key,city,zipcode);
    ```   

3. 查询方法

    在iginx上执行如下查询
    ```sql
    LOAD DATA FROM INFILE "/Users/janet/Desktop/Polystore-utils/dataCleaning/zipcode_city.csv" AS CSV INTO uszip(key,city,zipcode);
    SELECT count(a.zipcode) FROM uszip as a JOIN uszip as b ON a.zipcode = b.zipcode WHERE a.city <> b.city;
    ```
# 数据清洗说明文档

在iginx上执行如下查询
```sql
LOAD DATA FROM INFILE "/path/to/Polystore-utils/dataCleaning/zipcode_city.csv" AS CSV INTO uszip(key,zipcode,city);
SELECT count(a.zipcode) FROM uszip as a JOIN uszip as b ON a.zipcode = b.zipcode WHERE a.city <> b.city;
```
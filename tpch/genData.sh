#!/bin/bash

cd ../tpc/TPC-H\ V3.0.1/dbgen
./dbgen -s 1 -f
echo "数据生成完成"
# 源文件夹路径
source_folder="."

# 目标文件夹路径
destination_folder="../data"

# 确保目标文件夹存在，如果不存在则创建
mkdir -p "$destination_folder"

# 将所有*.tbl文件移动到目标文件夹
mv "$source_folder"/*.tbl "$destination_folder/"

echo "文件移动完成"

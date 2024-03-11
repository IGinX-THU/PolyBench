#!/bin/bash

# 从网站下载数据
wget https://snap.stanford.edu/data/web-Google.txt.gz
gzip -d web-Google.txt.gz
echo "数据下载完成"

# 源文件路径
input_file="web-google.txt"
# 输出文件路径
output_file="web-google.csv"
# 删除前4行并在文件开头插入一行
sed '1,4d' "$input_file" | (echo "fromnode,tonode" && cat) > "$output_file.temp"
# 将所有\t替换为","
sed 's/\t/,/g' "$output_file.temp" > "$output_file"
# 删除临时文件
rm "$output_file.temp"
rm "$input_file"
echo "操作完成"

# 可能需要执行：
# dos2unix web-google.csv





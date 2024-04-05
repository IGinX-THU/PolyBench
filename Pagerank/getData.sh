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
# 获取操作系统类型
os_type=$(uname -s)
# 判断操作系统类型
if [ "$os_type" == "Linux" ] || [ "$os_type" == "Darwin" ] || [ "$os_type" == "FreeBSD" ] || [ "$os_type" == "SunOS" ]; then
    echo "当前系统是Unix/Linux/Mac系统，执行dos2unix命令"
    dos2unix web-google.csv
elif [ "$os_type" == "CYGWIN_NT-10.0" ] || [ "$os_type" == "MINGW32_NT-6.1" ]; then
    echo "当前系统是Windows系统，不需要执行dos2unix命令"
else
    echo "未知的操作系统类型"
fi




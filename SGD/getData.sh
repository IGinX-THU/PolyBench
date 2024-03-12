#!/bin/bash

# 从网站下载数据
wget https://archive.ics.uci.edu/static/public/280/higgs.zip
echo "数据下载完成"
unzip higgs.zip
rm higgs.zip
gzip -d HIGGS.csv.gz
echo "数据解压完成"
sed 's/\.000000000000000000e+00//g' HIGGS.csv > HIGGS_new.csv
mv HIGGS_new.csv HIGGS.csv
echo "数据处理完成"


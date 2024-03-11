import queue
import pandas as pd
import numpy as np
import networkx as nx
import time


class UDSFpagerankall:
    def __init__(self):
        pass

    def transform(self, data, args, kvargs):
        start = time.time()
        res = self.buildHeader(data)
        # convert data[2:]to df
        df = pd.DataFrame(data[2:])
        # set df column index to data[0]
        df.columns = data[0]
        print(df.head())
        # train
        end = time.time()
        print('Dataframe conversion time: ', end - start)
        rank = pageRanking(df)
        rank = rank.to_numpy().tolist()
        for i in range(len(rank)):
            rank[i] = [int(rank[i][0]), rank[i][1]]
        res.extend(rank)  # data[2:] is the data
        all = time.time()
        print('Total time: ', all - start)
        return res

    def buildHeader(self, data):
        header = 'pagerankall(postgres.pagerankall.key, postgres.pagerankall.fromnode, postgres.pagerankall.tonode)'
        colNames = ["key", header]
        print(data[0])
        print(data[1])
        return [colNames, ["LONG", "DOUBLE"]]  # data[1] is the type


def pageRanking(df: pd.DataFrame):
    start = time.time()
    df = df.drop(['key'], axis=1)
    edges = [tuple(x) for x in df.values]
    G = nx.DiGraph()  # DiGraph()表示有向图
    for edge in edges:
        G.add_edge(edge[0], edge[1])  # 加入边
    print(G)
    pr_impro_value = nx.pagerank(G, alpha=0.85)
    end = time.time()
    print("calc_time: ", end - start)
    # convert key and values to pandas dataframe with column names 'key' and 'PageRank'
    ret = pd.DataFrame(pr_impro_value.items(), columns=['key', 'PageRank'])
    print(ret)
    ret['key'] = ret['key'].astype(np.int64)
    ret['PageRank'] = ret['PageRank'].astype(np.double)
    return ret
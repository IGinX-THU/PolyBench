package cn.edu.tsinghua.benchmark.item;

import java.util.ArrayList;
import java.util.List;

public class Bid extends Item {

    // id
    private int bidId;

    // 出价
    private int amount;

    // 时间戳
    private long timestamp;

    // 对应的用户id
    private long userId;

    // 对应的拍卖id
    private long auctionId;

    public Bid(int bidId, int amount, long timestamp, long userId, long auctionId) {
        this.bidId = bidId;
        this.amount = amount;
        this.timestamp = timestamp;
        this.userId = userId;
        this.auctionId = auctionId;
    }

    @Override
    public List<String> collectAttributes() {
        List<String> attributes = new ArrayList<>();
        attributes.add(String.valueOf(timestamp));
        attributes.add(String.valueOf(bidId));
        attributes.add(String.valueOf(amount));
        attributes.add(String.valueOf(userId));
        attributes.add(String.valueOf(auctionId));
        return attributes;
    }
}

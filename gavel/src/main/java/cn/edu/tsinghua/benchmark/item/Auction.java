package cn.edu.tsinghua.benchmark.item;

import java.util.ArrayList;
import java.util.List;

public class Auction extends Item {

    // id
    private int auctionId;

    // 题目
    private String title;

    // 描述
    private String description;

    // 开始日期
    private String startDate;

    // 结束日期
    private String endDate;

    // 对应的类别id
    private long categoryId;

    // 对应的用户id
    private long userId;

    public Auction(long time, int auctionId, String title, String description, String startDate, String endDate, long categoryId, long userId) {
        this.time = time;
        this.auctionId = auctionId;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.categoryId = categoryId;
        this.userId = userId;
    }

    @Override
    public List<String> collectAttributes() {
        List<String> attributes = new ArrayList<>();
        attributes.add((String.valueOf(time)));
        attributes.add(String.valueOf(auctionId));
        attributes.add(title);
        attributes.add(description);
        attributes.add(startDate);
        attributes.add(endDate);
        attributes.add(String.valueOf(categoryId));
        attributes.add(String.valueOf(userId));
        return attributes;
    }
}

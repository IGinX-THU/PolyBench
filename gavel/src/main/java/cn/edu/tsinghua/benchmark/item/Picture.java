package
        cn.edu.tsinghua.benchmark.item;

import java.util.ArrayList;
import java.util.List;

public class Picture extends Item {

    // 文件名
    private String filename;

    // 类型
    private int type;

    // 大小
    private long size;

    // 对应的拍卖id
    private long auctionId;

    public Picture(String filename, int type, long size, long auctionId) {
        this.filename = filename;
        this.type = type;
        this.size = size;
        this.auctionId = auctionId;
    }

    @Override
    public List<String> collectAttributes() {
        List<String> attributes = new ArrayList<>();
        attributes.add(filename);
        attributes.add(String.valueOf(type));
        attributes.add(String.valueOf(size));
        attributes.add(String.valueOf(auctionId));
        return attributes;
    }
}

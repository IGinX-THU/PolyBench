package cn.edu.tsinghua.benchmark.item;

import java.util.ArrayList;
import java.util.List;

public class Category extends Item {

    // id
    private int categoryId;

    // 名称
    private String name;

    public Category(int categoryId, String name) {
        this.categoryId = categoryId;
        this.name = name;
    }

    @Override
    public List<String> collectAttributes() {
        List<String> attributes = new ArrayList<>();
        attributes.add(String.valueOf(categoryId));
        attributes.add(name);
        return attributes;
    }
}

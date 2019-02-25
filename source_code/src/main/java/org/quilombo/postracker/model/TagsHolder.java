package org.quilombo.postracker.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TagsHolder {

    private Map<String, Tag> tags = new ConcurrentHashMap<>();

    public TagsHolder() {
    }

    public void setPos(String id, double x, double y, double z) {
        Tag tag = tags.get(id);
        if (tag == null) {
            tag = new Tag(id);
            tags.put(id, tag);
        }
        tag.setPos(x, y, z);
    }

    public List<Tag> getTags() {
        return new ArrayList<>(tags.values());
    }
}

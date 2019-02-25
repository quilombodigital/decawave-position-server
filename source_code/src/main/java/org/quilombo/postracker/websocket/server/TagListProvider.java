package org.quilombo.postracker.websocket.server;

import org.quilombo.postracker.model.Tag;

import java.util.List;

public interface TagListProvider {
    List<Tag> getTags();
}

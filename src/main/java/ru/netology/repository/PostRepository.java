package ru.netology.repository;

import ru.netology.model.Post;
import org.springframework.stereotype.Repository;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
// Stub
public class PostRepository {
    private final ConcurrentHashMap<Long, Post> postMap = new ConcurrentHashMap<>();
    private final AtomicLong id = new AtomicLong();

    public Collection<Post> all() {
        return postMap.values();
    }

    public Optional<Post> getById(long id) {
        return Optional.ofNullable(postMap.get(id));
    }

    public Post save(Post post) {
        if (post.getId() == 0) {
            long i = id.incrementAndGet();
            postMap.put(i, new Post(i, post.getContent()));

        } else if (post.getId() != 0) {
            Long currentId = post.getId();
            postMap.put(currentId, new Post(post.getId(), post.getContent()));
            id.incrementAndGet();
        }
        return post;
    }

    public void removeById(long id) {
        postMap.remove(id);
    }
}

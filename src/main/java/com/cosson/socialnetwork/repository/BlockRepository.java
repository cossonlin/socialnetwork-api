package com.cosson.socialnetwork.repository;

import com.cosson.socialnetwork.entity.Block;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlockRepository extends CrudRepository<Block, Long> {
    Optional<List<Block>> findByRequestor(String requestor);

    Optional<List<Block>> findByTarget(String target);

    Optional<Block> findByRequestorAndTarget(String requestor, String target);
}

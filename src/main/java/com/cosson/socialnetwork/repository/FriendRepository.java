package com.cosson.socialnetwork.repository;

import com.cosson.socialnetwork.entity.Friends;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRepository extends CrudRepository<Friends, Long> {
    List<Friends> findByRequestor(String requestor);

    List<Friends> findByAcceptor(String acceptor);
}

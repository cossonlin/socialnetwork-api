package com.cosson.socialnetwork.repository;

import com.cosson.socialnetwork.entity.Subscription;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends CrudRepository<Subscription, Long> {
    Optional<Subscription> findByRequestorAndTarget(String requestor, String target);

    Optional<List<Subscription>> findByTarget(String target);
}

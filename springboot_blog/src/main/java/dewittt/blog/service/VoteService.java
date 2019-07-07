package dewittt.blog.service;

import dewittt.blog.entity.Vote;

import java.util.Optional;

public interface VoteService {

    Optional<Vote> getVoteById(Long id);
    void removeVote(Long id);
}

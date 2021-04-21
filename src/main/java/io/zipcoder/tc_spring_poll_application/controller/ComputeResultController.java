package io.zipcoder.tc_spring_poll_application.controller;

import dtos.OptionCount;
import dtos.VoteResult;
import io.zipcoder.tc_spring_poll_application.domain.Option;
import io.zipcoder.tc_spring_poll_application.domain.Vote;
import io.zipcoder.tc_spring_poll_application.repositories.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;

@RestController
public class ComputeResultController {

    private final VoteRepository voteRepository;

    @Autowired
    public ComputeResultController(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    @RequestMapping(value = "/computeresult", method = RequestMethod.GET)
    public ResponseEntity<?> computeResult(@RequestParam Long pollId) {
        VoteResult voteResult = new VoteResult();
        Iterable<Vote> allVotes = voteRepository.findVotesByPoll(pollId);
        //TODO: Implement algorithm to count votes
        //create the Collection
        voteResult.setResults(new ArrayList<OptionCount>());
        Collection<OptionCount> list = voteResult.getResults();
        for (Vote vote : allVotes) {
            //sets voteResult total
            voteResult.incrementTotal();
            //if empty, add a new one
            if (list.isEmpty()) {
                list.add(new OptionCount(vote.getId(), 1));
                break;
            }
            boolean listContains = false;
            //false if list doesn't contain optionId
            for (OptionCount oc : voteResult.getResults()) {
                //if it does, increase the count, set to true
                if (vote.getOption().getId().equals(oc.getOptionId())) {
                    oc.increaseCount();
                    listContains = true;
                }
            }
            //if list didn't contain, add new OptionCount
            if (!listContains) {
                list.add(new OptionCount(vote.getId(), 1));
            }
        }
        voteResult.setResults(list);
        return new ResponseEntity<VoteResult>(voteResult, HttpStatus.OK);
    }
}

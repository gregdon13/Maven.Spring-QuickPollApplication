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
import java.util.Collections;

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
        }
        //TotalVotes works

        //Keep for now
        for (Vote vote : allVotes) {
            if (list.isEmpty()) {
                OptionCount random = new OptionCount(vote.getOption().getId(), 0);
                list.add(random);
            }
            Long temp = vote.getOption().getId();
            boolean present = true;
            for (OptionCount oc : list) {
                present = false;
                if (oc.getOptionId().equals(temp)) {
                    oc.increaseCount();
                    present = true;
                }
            }
            if (!present) {
                OptionCount anotherOne = new OptionCount(vote.getOption().getId(), 0);
                list.add(anotherOne);
                anotherOne.increaseCount();
            }
        }

        voteResult.setResults(list);
        return new ResponseEntity<VoteResult>(voteResult, HttpStatus.OK);
    }
}

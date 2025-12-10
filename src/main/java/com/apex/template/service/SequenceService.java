package com.apex.template.service;




import com.apex.template.domain.Sequence;
import com.apex.template.repository.SequenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;


@Service
@RequiredArgsConstructor(onConstructor=@__(@Autowired))
public class SequenceService {
    private final SequenceRepository sequenceRepository;


    public Sequence save(Sequence sequence) {
        return sequenceRepository.save(sequence);
    }

    public synchronized Long incrementSequence() {
        Sequence sequence = sequenceRepository.getFirstById(1L);
        final AtomicLong orderReference=new AtomicLong(sequence.getSequenceNo());
        sequence.setSequenceNo(sequence.getSequenceNo() + 1);
        sequenceRepository.save(sequence).getSequenceNo();
        return orderReference.incrementAndGet();
    }
}

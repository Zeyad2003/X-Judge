package com.xjudge.service.sample;

import com.xjudge.entity.Sample;
import com.xjudge.repository.SampleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SampleServiceImpl implements SampleService {

    private final SampleRepository sampleRepository;

    @Override
    public void saveSample(Sample sample) {
        sampleRepository.save(sample);
    }

    @Override
    public List<Sample> saveAll(Iterable<Sample> samples) {
        return sampleRepository.saveAll(samples);
    }
}

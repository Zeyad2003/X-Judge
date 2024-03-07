package com.xjudge.service.sample;

import com.xjudge.entity.Sample;

import java.util.List;

public interface SampleService {
    void saveSample(Sample sample);
    List<Sample> saveAll(Iterable<Sample> samples);
}

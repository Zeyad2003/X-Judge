package com.xjudge.service.compiler;

import com.xjudge.entity.Compiler;
import com.xjudge.exception.XJudgeException;
import com.xjudge.model.enums.OnlineJudgeType;
import com.xjudge.repository.CompilerRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompilerServiceImpl implements CompilerService{
    private final CompilerRepo compilerRepo;

    @Override
    public List<Compiler> getCompilersByOnlineJudgeType(String onlineJudge) {
        OnlineJudgeType ojType;

        try {
            ojType = OnlineJudgeType.valueOf(onlineJudge);
        } catch (Exception e) {
            throw new XJudgeException("Invalid Online Judge Type", CompilerServiceImpl.class.getName(), HttpStatus.BAD_REQUEST);
        }

        return compilerRepo.findByOnlineJudgeType(ojType);
    }
}

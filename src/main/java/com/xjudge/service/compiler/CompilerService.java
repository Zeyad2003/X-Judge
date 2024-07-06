package com.xjudge.service.compiler;

import com.xjudge.entity.Compiler;
import com.xjudge.model.enums.OnlineJudgeType;

import java.util.List;
import java.util.Optional;

public interface CompilerService {

    List<Compiler> getCompilersByOnlineJudgeType(String onlineJudgeType);
    Compiler getCompilerByIdValue(String idValue);
}

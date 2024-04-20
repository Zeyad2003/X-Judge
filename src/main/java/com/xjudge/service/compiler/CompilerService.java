package com.xjudge.service.compiler;

import com.xjudge.entity.Compiler;
import com.xjudge.model.enums.OnlineJudgeType;

import java.util.List;

public interface CompilerService {

    List<Compiler> getCompilersByOnlineJudgeType(String onlineJudgeType);
}

package com.xjudge.repository;

import com.xjudge.entity.Compiler;
import com.xjudge.model.enums.OnlineJudgeType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompilerRepo extends JpaRepository<Compiler, Long> {

    List<Compiler> findByOnlineJudgeType(OnlineJudgeType onlineJudgeType);
    Optional<Compiler> findCompilerByIdValue(String idValue);

}

package com.xjudge.controller.problem;

import com.xjudge.service.problem.ProblemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class ProblemDescription {

    private final ProblemService problemService;

    @GetMapping("/description/{source}-{code}")
    public String getProblemDescription(@PathVariable String source,
                                        @PathVariable String code,
                                        Model model) {
        model.addAttribute("problem", problemService.getProblemDescription(source, code));
        return "problem-description";
    }

}

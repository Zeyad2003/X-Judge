package com.xjudge.util;

import com.xjudge.entity.Compiler;
import com.xjudge.entity.User;
import com.xjudge.model.enums.OnlineJudgeType;
import com.xjudge.model.enums.UserRole;
import com.xjudge.repository.CompilerRepo;
import com.xjudge.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CommandLineStartupRunner implements CommandLineRunner {
    private final UserRepo userRepo;
    private final CompilerRepo compilerRepo;
    private final PasswordEncoder encoder;

    @Override
    public void run(String... args) {
        addUser();

        if(compilerRepo.count() == 0) {
            addCodeForcesCompilerList();
            addAtCoderCompilerList();
        }
    }

    private void addUser() {
        User user = User.builder()
                .id(1L)
                .role(UserRole.ADMIN)
                .handle("Zeyad_Nasef")
                .email("zeyad@gmail.com")
                .password(encoder.encode("123456"))
                .firstName("Zeyad")
                .lastName("Nasef")
                .school("MUFCI")
                .registrationDate(LocalDate.now())
                .solvedCount(0L)
                .attemptedCount(0L)
                .isVerified(true)
                .build();
        if(userRepo.findByHandle(user.getHandle()).isEmpty()) userRepo.save(user);
    }

    private void addCodeForcesCompilerList() {
        List<Compiler> compilerList = List.of(
                Compiler.builder().idValue("43").name("GNU GCC C11 5.1.0").onlineJudgeType(OnlineJudgeType.CodeForces).build(),
                Compiler.builder().idValue("50").name("GNU G++14 6.4.0").onlineJudgeType(OnlineJudgeType.CodeForces).build(),
                Compiler.builder().idValue("54").name("GNU G++17 7.3.0").onlineJudgeType(OnlineJudgeType.CodeForces).build(),
                Compiler.builder().idValue("89").name("GNU G++20 13.2 (64 bit, winlibs)").onlineJudgeType(OnlineJudgeType.CodeForces).build(),
                Compiler.builder().idValue("65").name("C# 8, .NET Core 3.1").onlineJudgeType(OnlineJudgeType.CodeForces).build(),
                Compiler.builder().idValue("79").name("C# 10, .NET SDK 6.0").onlineJudgeType(OnlineJudgeType.CodeForces).build(),
                Compiler.builder().idValue("9").name("C# Mono 6.8").onlineJudgeType(OnlineJudgeType.CodeForces).build(),
                Compiler.builder().idValue("28").name("D DMD32 v2.105.0").onlineJudgeType(OnlineJudgeType.CodeForces).build(),
                Compiler.builder().idValue("32").name("Go 1.22.2").onlineJudgeType(OnlineJudgeType.CodeForces).build(),
                Compiler.builder().idValue("12").name("Haskell GHC 8.10.1").onlineJudgeType(OnlineJudgeType.CodeForces).build(),
                Compiler.builder().idValue("87").name("Java 21 64bit").onlineJudgeType(OnlineJudgeType.CodeForces).build(),
                Compiler.builder().idValue("36").name("Java 8 32bit").onlineJudgeType(OnlineJudgeType.CodeForces).build(),
                Compiler.builder().idValue("83").name("Kotlin 1.7.20").onlineJudgeType(OnlineJudgeType.CodeForces).build(),
                Compiler.builder().idValue("88").name("Kotlin 1.9.21").onlineJudgeType(OnlineJudgeType.CodeForces).build(),
                Compiler.builder().idValue("19").name("OCaml 4.02.1").onlineJudgeType(OnlineJudgeType.CodeForces).build(),
                Compiler.builder().idValue("3").name("Delphi 7").onlineJudgeType(OnlineJudgeType.CodeForces).build(),
                Compiler.builder().idValue("4").name("Free Pascal 3.2.2").onlineJudgeType(OnlineJudgeType.CodeForces).build(),
                Compiler.builder().idValue("51").name("PascalABC.NET 3.8.3").onlineJudgeType(OnlineJudgeType.CodeForces).build(),
                Compiler.builder().idValue("13").name("Perl 5.20.1").onlineJudgeType(OnlineJudgeType.CodeForces).build(),
                Compiler.builder().idValue("6").name("PHP 8.1.7").onlineJudgeType(OnlineJudgeType.CodeForces).build(),
                Compiler.builder().idValue("7").name("Python 2.7.18").onlineJudgeType(OnlineJudgeType.CodeForces).build(),
                Compiler.builder().idValue("31").name("Python 3.8.10").onlineJudgeType(OnlineJudgeType.CodeForces).build(),
                Compiler.builder().idValue("40").name("PyPy 2.7.13 (7.3.0)").onlineJudgeType(OnlineJudgeType.CodeForces).build(),
                Compiler.builder().idValue("41").name("PyPy 3.6.9 (7.3.0)").onlineJudgeType(OnlineJudgeType.CodeForces).build(),
                Compiler.builder().idValue("70").name("PyPy 3.10 (7.3.15, 64bit)").onlineJudgeType(OnlineJudgeType.CodeForces).build(),
                Compiler.builder().idValue("67").name("Ruby 3.2.2").onlineJudgeType(OnlineJudgeType.CodeForces).build(),
                Compiler.builder().idValue("75").name("Rust 1.75.0 (2021)").onlineJudgeType(OnlineJudgeType.CodeForces).build(),
                Compiler.builder().idValue("20").name("Scala 2.12.8").onlineJudgeType(OnlineJudgeType.CodeForces).build(),
                Compiler.builder().idValue("34").name("JavaScript V8 4.8.0").onlineJudgeType(OnlineJudgeType.CodeForces).build(),
                Compiler.builder().idValue("55").name("Node.js 15.8.0 (64bit)").onlineJudgeType(OnlineJudgeType.CodeForces).build()
        );

        compilerRepo.saveAll(compilerList);
    }

    private void addAtCoderCompilerList() {
        List<Compiler> compilerList = List.of(
                Compiler.builder().idValue("5001").name("C++ 20 (gcc 12.2)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5002").name("Go (go 1.20.6)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5003").name("C# 11.0 (.NET 7.0.7)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5004").name("Kotlin (Kotlin/JVM 1.8.20)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5005").name("Java (OpenJDK 17)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5006").name("Nim (Nim 1.6.14)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5007").name("V (V 0.4)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5008").name("Zig (Zig 0.10.1)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5009").name("JavaScript (Node.js 18.16.1)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5010").name("JavaScript (Deno 1.35.1)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5011").name("R (GNU R 4.2.1)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5012").name("D (DMD 2.104.0)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5013").name("D (LDC 1.32.2)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5014").name("Swift (swift 5.8.1)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5015").name("Dart (Dart 3.0.5)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5016").name("PHP (php 8.2.8)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5017").name("C (gcc 12.2.0)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5018").name("Ruby (ruby 3.2.2)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5019").name("Crystal (Crystal 1.9.1)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5020").name("Brainfuck (bf 20041219)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5021").name("F# 7.0 (.NET 7.0.7)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5022").name("Julia (Julia 1.9.2)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5023").name("Bash (bash 5.2.2)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5024").name("Text (cat 8.32)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5025").name("Haskell (GHC 9.4.5)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5026").name("Fortran (gfortran 12.2)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5027").name("Lua (LuaJIT 2.1.0-beta3)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5028").name("C++ 23 (gcc 12.2)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5029").name("Common Lisp (SBCL 2.3.6)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5030").name("COBOL (Free) (GnuCOBOL 3.1.2)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5031").name("C++ 23 (Clang 16.0.6)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5032").name("Zsh (Zsh 5.9)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5033").name("SageMath (SageMath 9.5)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5034").name("Sed (GNU sed 4.8)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5035").name("bc (bc 1.07.1)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5036").name("dc (dc 1.07.1)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5037").name("Perl (perl  5.34)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5038").name("AWK (GNU Awk 5.0.1)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5039").name("なでしこ (cnako3 3.4.20)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5040").name("Assembly x64 (NASM 2.15.05)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5041").name("Pascal (fpc 3.2.2)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5042").name("C# 11.0 AOT (.NET 7.0.7)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5043").name("Lua (Lua 5.4.6)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5044").name("Prolog (SWI-Prolog 9.0.4)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5045").name("PowerShell (PowerShell 7.3.1)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5046").name("Scheme (Gauche 0.9.12)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5047").name("Scala 3.3.0 (Scala Native 0.4.14)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5048").name("Visual Basic 16.9 (.NET 7.0.7)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5049").name("Forth (gforth 0.7.3)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5050").name("Clojure (babashka 1.3.181)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5051").name("Erlang (Erlang 26.0.2)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5052").name("TypeScript 5.1 (Deno 1.35.1)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5053").name("C++ 17 (gcc 12.2)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5054").name("Rust (rustc 1.70.0)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5055").name("Python (CPython 3.11.4)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5056").name("Scala (Dotty 3.3.0)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5057").name("Koka (koka 2.4.0)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5058").name("TypeScript 5.1 (Node.js 18.16.1)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5059").name("OCaml (ocamlopt 5.0.0)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5060").name("Raku (Rakudo 2023.06)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5061").name("Vim (vim 9.0.0242)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5062").name("Emacs Lisp (Native Compile) (GNU Emacs 28.2)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5063").name("Python (Mambaforge / CPython 3.10.10)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5064").name("Clojure (clojure 1.11.1)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5065").name("プロデル (mono版プロデル 1.9.1182)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5066").name("ECLiPSe (ECLiPSe 7.1_13)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5067").name("Nibbles (literate form) (nibbles 1.01)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5068").name("Ada (GNAT 12.2)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5069").name("jq (jq 1.6)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5070").name("Cyber (Cyber v0.2-Latest)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5071").name("Carp (Carp 0.5.5)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5072").name("C++ 17 (Clang 16.0.6)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5073").name("C++ 20 (Clang 16.0.6)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5074").name("LLVM IR (Clang 16.0.6)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5075").name("Emacs Lisp (Byte Compile) (GNU Emacs 28.2)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5076").name("Factor (Factor 0.98)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5077").name("D (GDC 12.2)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5078").name("Python (PyPy 3.10-v7.3.12)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5079").name("Whitespace (whitespacers 1.0.0)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5080").name(">>&lt;&gt; (fishr 0.1.0)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5081").name("ReasonML (reason 3.9.0)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5082").name("Python (Cython 0.29.34)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5083").name("Octave (GNU Octave 8.2.0)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5084").name("Haxe (JVM) (Haxe 4.3.1)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5085").name("Elixir (Elixir 1.15.2)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5086").name("Mercury (Mercury 22.01.6)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5087").name("Seed7 (Seed7 3.2.1)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5088").name("Emacs Lisp (No Compile) (GNU Emacs 28.2)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5089").name("Unison (Unison M5b)").onlineJudgeType(OnlineJudgeType.AtCoder).build(),
                Compiler.builder().idValue("5090").name("COBOL (GnuCOBOL(Fixed) 3.1.2)").onlineJudgeType(OnlineJudgeType.AtCoder).build()
        );

        compilerRepo.saveAll(compilerList);
    }
}

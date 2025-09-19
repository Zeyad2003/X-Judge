package com.xjudge.config.audit;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;

public class AuditorAwareImpl implements AuditorAware<String> {

    @NonNull
    @Override
    public Optional<String> getCurrentAuditor() {
        // TODO: Return a test user until security is implemented
        return Optional.of("test-user");
    }
}

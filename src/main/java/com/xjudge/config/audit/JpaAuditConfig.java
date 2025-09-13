package com.xjudge.config.audit;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/** Auditing configuration for JPA entities. (AKA. Created/Updated timestamps) */
@Configuration
@EnableJpaAuditing
public class JpaAuditConfig {}

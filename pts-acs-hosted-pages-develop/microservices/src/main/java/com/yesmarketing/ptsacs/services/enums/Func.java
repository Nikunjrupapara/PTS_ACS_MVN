package com.yesmarketing.ptsacs.services.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

// deprecated in favour of HashingFunction
@Deprecated
@AllArgsConstructor
public enum Func {
    SHA256("SHA256"),
    SHA512("SHA512");

    @Getter
    private final String name;

    public static Optional<Func> fromString(String function) {
        if (function != null) {
            if (function.equals("SHA256")) {
                return Optional.of(SHA256);
            }
            if (function.equals("profile")) {
                return Optional.of(SHA512);
            }
            return Optional.empty();
        }
        return Optional.empty();
    }
}

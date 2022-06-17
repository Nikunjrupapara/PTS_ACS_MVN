package com.yesmarketing.ptsacs.services.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

@AllArgsConstructor
public enum View {
    FULL("full"),
    PROFILE("profile"),
    SERVICES("services");

    @Getter
    private final String name;

    public static Optional<View> fromString(String view) {
        if (view != null) {
            if (view.equals("full")) {
                return Optional.of(FULL);
            }
            if (view.equals("profile")) {
                return Optional.of(PROFILE);
            }
            if (view.equals("services")) {
                return Optional.of(SERVICES);
            }
            return Optional.empty();
        }
        return Optional.of(PROFILE);
    }
}

package com.agms.crop_service.model;

import java.util.Set;

/**
 * Allowed crop lifecycle states with enforced one-direction transitions.
 *
 * Valid transitions:
 *   SEEDLING → VEGETATIVE
 *   VEGETATIVE → HARVESTED
 *
 * Any other transition (e.g. HARVESTED → SEEDLING) is rejected.
 */
public enum CropStatus {

    SEEDLING(Set.of("VEGETATIVE")),
    VEGETATIVE(Set.of("HARVESTED")),
    HARVESTED(Set.of()); // terminal state — no further transitions allowed

    private final Set<String> allowedNext;

    CropStatus(Set<String> allowedNext) {
        this.allowedNext = allowedNext;
    }

    /**
     * Returns true if transitioning from this state to {@code target} is allowed.
     */
    public boolean canTransitionTo(CropStatus target) {
        return allowedNext.contains(target.name());
    }
}

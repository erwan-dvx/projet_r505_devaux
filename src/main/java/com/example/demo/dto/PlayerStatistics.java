package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PlayerStatistics(
        String statut,
        @JsonProperty("poste_prefere") String preferredPosition,
        int titularisations,
        int remplacements,
        @JsonProperty("moyenne_evaluations") Double averageRatings,
        @JsonProperty("pourcentage_victoires") double winPercentage,
        @JsonProperty("selections_consecutives") int consecutiveSelections,
        @JsonProperty("total_participations") int totalParticipations) {
}

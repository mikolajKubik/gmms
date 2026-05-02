package net.mkubik.gmms.e2e;

import net.mkubik.gmms.api.model.CreateGymResponse;
import net.mkubik.gmms.api.model.CreateMembershipPlanResponse;
import net.mkubik.gmms.api.model.ListMembershipPlansResponse;
import net.mkubik.gmms.api.model.MembershipPlanEntry;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClientResponseException;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MembershipPlanE2ETest extends BaseE2ETest {

    @Test
    void createAndListMembershipPlansForGym() {
        CreateGymResponse gym = createGym(
                "Prime Fitness",
                "Fitness Ave 10, Warsaw",
                "+48122000111"
        );

        CreateMembershipPlanResponse plan = createMembershipPlan(
                gym.getId(),
                "Evening BASIC",
                "BASIC",
                new BigDecimal("79.99"),
                "EUR",
                6,
                25
        );

        assertThat(plan.getId()).isNotNull();
        assertThat(plan.getActiveMembers()).isEqualTo(0);

        int dbCount = countRows(
                "SELECT COUNT(*) FROM membership_plan WHERE id = ? AND gym_id = ?",
                plan.getId(),
                gym.getId()
        );
        assertThat(dbCount).isEqualTo(1);

        ListMembershipPlansResponse listResponse = restClient.get()
                .uri("/gyms/{gymId}/plans", gym.getId())
                .retrieve()
                .body(ListMembershipPlansResponse.class);

        assertThat(listResponse).isNotNull();
        assertThat(listResponse.getPlans())
                .extracting(MembershipPlanEntry::getId)
                .contains(plan.getId());
        assertThat(listResponse.getPage()).isPresent();
    }

    @Test
    void createMembershipPlanForMissingGymReturnsNotFound() {
        UUID missingGymId = UUID.randomUUID();

        assertThatThrownBy(() -> createMembershipPlan(
                missingGymId,
                "Missing Gym BASIC",
                "BASIC",
                new BigDecimal("39.99"),
                "EUR",
                1,
                10
        ))
                .isInstanceOf(RestClientResponseException.class)
                .satisfies(ex -> {
                    RestClientResponseException responseException = (RestClientResponseException) ex;
                    assertThat(responseException.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
                });
    }
}


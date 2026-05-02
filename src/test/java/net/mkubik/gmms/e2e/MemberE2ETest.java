package net.mkubik.gmms.e2e;

import net.mkubik.gmms.api.model.CancelMembershipResponse;
import net.mkubik.gmms.api.model.CreateGymResponse;
import net.mkubik.gmms.api.model.CreateMembershipPlanResponse;
import net.mkubik.gmms.api.model.ListMembersResponse;
import net.mkubik.gmms.api.model.MemberEntry;
import net.mkubik.gmms.api.model.MembershipStatus;
import net.mkubik.gmms.api.model.RegisterMemberResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClientResponseException;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberE2ETest extends BaseE2ETest {

    @Test
    void registerListAndCancelMember() {
        CreateGymResponse gym = createGym(
                "Nordic Gym",
                "North Road 7, Warsaw",
                "+48220001122"
        );

        CreateMembershipPlanResponse plan = createMembershipPlan(
                gym.getId(),
                "Morning PREMIUM",
                "PREMIUM",
                new BigDecimal("129.00"),
                "EUR",
                12,
                5
        );

        RegisterMemberResponse member = registerMember(
                plan.getId(),
                "Jan Kowalski",
                "jan.kowalski@example.com"
        );

        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).contains(MembershipStatus.ACTIVE);
        assertThat(member.getPlanName()).contains(plan.getName());

        String status = jdbcTemplate.queryForObject(
                "SELECT status FROM member WHERE id = ?",
                String.class,
                member.getId()
        );
        assertThat(status).isEqualTo("ACTIVE");

        Integer activeMembers = jdbcTemplate.queryForObject(
                "SELECT active_members FROM membership_plan WHERE id = ?",
                Integer.class,
                plan.getId()
        );
        assertThat(activeMembers).isEqualTo(1);

        ListMembersResponse listResponse = restClient.get()
                .uri("/members")
                .retrieve()
                .body(ListMembersResponse.class);

        assertThat(listResponse).isNotNull();
        assertThat(listResponse.getMembers())
                .extracting(MemberEntry::getId)
                .contains(member.getId());

        MemberEntry listEntry = listResponse.getMembers().stream()
                .filter(entry -> entry.getId().equals(member.getId()))
                .findFirst()
                .orElseThrow();

        assertThat(listEntry.getPlanName()).contains(plan.getName());
        assertThat(listEntry.getGymName()).contains(gym.getName());

        CancelMembershipResponse cancelled = restClient.patch()
                .uri("/members/{memberId}/cancel", member.getId())
                .retrieve()
                .body(CancelMembershipResponse.class);

        assertThat(cancelled).isNotNull();
        assertThat(cancelled.getStatus()).isEqualTo(MembershipStatus.CANCELLED);

        String cancelledStatus = jdbcTemplate.queryForObject(
                "SELECT status FROM member WHERE id = ?",
                String.class,
                member.getId()
        );
        assertThat(cancelledStatus).isEqualTo("CANCELLED");

        Integer updatedActiveMembers = jdbcTemplate.queryForObject(
                "SELECT active_members FROM membership_plan WHERE id = ?",
                Integer.class,
                plan.getId()
        );
        assertThat(updatedActiveMembers).isEqualTo(0);
    }

    @Test
    void registerBeyondPlanCapacityReturnsConflict() {
        CreateGymResponse gym = createGym(
                "City Gym",
                "City Road 5, Warsaw",
                "+48220003344"
        );

        CreateMembershipPlanResponse plan = createMembershipPlan(
                gym.getId(),
                "Limited BASIC",
                "BASIC",
                new BigDecimal("49.00"),
                "EUR",
                1,
                1
        );

        RegisterMemberResponse first = registerMember(
                plan.getId(),
                "Ala Nowak",
                "ala.nowak@example.com"
        );
        assertThat(first.getId()).isNotNull();

        assertThatThrownBy(() -> registerMember(
                plan.getId(),
                "Piotr Zielinski",
                "piotr.zielinski@example.com"
        ))
                .isInstanceOf(RestClientResponseException.class)
                .satisfies(ex -> {
                    RestClientResponseException responseException = (RestClientResponseException) ex;
                    assertThat(responseException.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
                });
    }

    @Test
    void cancelAlreadyCancelledMembershipReturnsConflict() {
        CreateGymResponse gym = createGym(
                "MetroFlex",
                "Sky Road 12, Warsaw",
                "+48221112233"
        );

        CreateMembershipPlanResponse plan = createMembershipPlan(
                gym.getId(),
                "Standard BASIC",
                "BASIC",
                new BigDecimal("59.00"),
                "EUR",
                3,
                10
        );

        RegisterMemberResponse member = registerMember(
                plan.getId(),
                "Ewa Kowalska",
                "ewa.kowalska@example.com"
        );

        restClient.patch()
                .uri("/members/{memberId}/cancel", member.getId())
                .retrieve()
                .toBodilessEntity();

        assertThatThrownBy(() -> restClient.patch()
                .uri("/members/{memberId}/cancel", member.getId())
                .retrieve()
                .toBodilessEntity())
                .isInstanceOf(RestClientResponseException.class)
                .satisfies(ex -> {
                    RestClientResponseException responseException = (RestClientResponseException) ex;
                    assertThat(responseException.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
                });
    }
}


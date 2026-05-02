package net.mkubik.gmms.e2e;

import net.mkubik.gmms.api.model.CreateGymResponse;
import net.mkubik.gmms.api.model.CreateMembershipPlanResponse;
import net.mkubik.gmms.api.model.RevenueReportEntry;
import net.mkubik.gmms.api.model.RevenueReportResponse;
import net.mkubik.gmms.api.model.RegisterMemberResponse;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class ReportE2ETest extends BaseE2ETest {

    @Test
    void revenueReportAggregatesActiveMembersByGymAndCurrency() {
        CreateGymResponse gym = createGym(
                "Revenue Gym",
                "Market Street 1, Warsaw",
                "+48221110000"
        );

        CreateMembershipPlanResponse plan = createMembershipPlan(
                gym.getId(),
                "Revenue BASIC",
                "BASIC",
                new BigDecimal("50.00"),
                "EUR",
                3,
                10
        );

        RegisterMemberResponse first = registerMember(
                plan.getId(),
                "Member One",
                "member.one@example.com"
        );
        RegisterMemberResponse second = registerMember(
                plan.getId(),
                "Member Two",
                "member.two@example.com"
        );

        assertThat(first.getId()).isNotNull();
        assertThat(second.getId()).isNotNull();

        RevenueReportResponse initialReport = restClient.get()
                .uri("/gyms/revenue")
                .retrieve()
                .body(RevenueReportResponse.class);

        assertThat(initialReport).isNotNull();
        RevenueReportEntry entry = findReportEntry(initialReport, gym.getName());
        assertThat(entry.getCurrency()).contains("EUR");
        BigDecimal initialAmount = entry.getAmount().orElseThrow();
        assertThat(initialAmount).isEqualByComparingTo("100.00");

        restClient.patch()
                .uri("/members/{memberId}/cancel", first.getId())
                .retrieve()
                .toBodilessEntity();

        RevenueReportResponse afterCancelReport = restClient.get()
                .uri("/gyms/revenue")
                .retrieve()
                .body(RevenueReportResponse.class);

        assertThat(afterCancelReport).isNotNull();
        RevenueReportEntry updatedEntry = findReportEntry(afterCancelReport, gym.getName());
        BigDecimal updatedAmount = updatedEntry.getAmount().orElseThrow();
        assertThat(updatedAmount).isEqualByComparingTo("50.00");
    }

    private RevenueReportEntry findReportEntry(RevenueReportResponse response, String gymName) {
        return response.getReport().stream()
                .filter(entry -> entry.getGymName().orElse("").equals(gymName))
                .findFirst()
                .orElseThrow();
    }
}

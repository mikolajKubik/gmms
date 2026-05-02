package net.mkubik.gmms.e2e;

import net.mkubik.gmms.api.model.CreateGymRequest;
import net.mkubik.gmms.api.model.CreateGymResponse;
import net.mkubik.gmms.api.model.CreateMembershipPlanRequest;
import net.mkubik.gmms.api.model.CreateMembershipPlanResponse;
import net.mkubik.gmms.api.model.RegisterMemberRequest;
import net.mkubik.gmms.api.model.RegisterMemberResponse;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class BaseE2ETest {

    @LocalServerPort
    protected int port;

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    protected RestClient restClient;

    @BeforeEach
    void setUpRestClientAndCleanDb() {
        restClient = RestClient.builder()
                .baseUrl("http://localhost:" + port + "/api/v1")
                .build();
        cleanDatabase();
    }

    protected void cleanDatabase() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.execute("TRUNCATE TABLE member");
        jdbcTemplate.execute("TRUNCATE TABLE membership_plan");
        jdbcTemplate.execute("TRUNCATE TABLE gym");
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }

    protected CreateGymResponse createGym(String name, String address, String phoneNumber) {
        CreateGymRequest request = new CreateGymRequest();
        request.setName(name);
        request.setAddress(address);
        request.setPhoneNumber(phoneNumber);

        CreateGymResponse response = restClient.post()
                .uri("/gyms")
                .body(request)
                .retrieve()
                .body(CreateGymResponse.class);

        assertThat(response).isNotNull();
        return response;
    }

    protected CreateMembershipPlanResponse createMembershipPlan(
            UUID gymId,
            String name,
            String planTypeCode,
            BigDecimal price,
            String currency,
            int durationMonths,
            int maxMembers
    ) {
        CreateMembershipPlanRequest request = new CreateMembershipPlanRequest();
        request.setName(name);
        request.setPlanTypeCode(planTypeCode);
        request.setPrice(price);
        request.setCurrency(currency);
        request.setDurationMonths(durationMonths);
        request.setMaxMembers(maxMembers);

        CreateMembershipPlanResponse response = restClient.post()
                .uri("/gyms/{gymId}/plans", gymId)
                .body(request)
                .retrieve()
                .body(CreateMembershipPlanResponse.class);

        assertThat(response).isNotNull();
        return response;
    }

    protected RegisterMemberResponse registerMember(UUID planId, String fullName, String email) {
        RegisterMemberRequest request = new RegisterMemberRequest();
        request.setFullName(fullName);
        request.setEmail(email);

        RegisterMemberResponse response = restClient.post()
                .uri("/plans/{planId}/members", planId)
                .body(request)
                .retrieve()
                .body(RegisterMemberResponse.class);

        assertThat(response).isNotNull();
        return response;
    }

    protected int countRows(String sql, Object... args) {
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, args);
        return count == null ? 0 : count;
    }
}

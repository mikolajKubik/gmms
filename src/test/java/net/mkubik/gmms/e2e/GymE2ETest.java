package net.mkubik.gmms.e2e;

import net.mkubik.gmms.api.model.CreateGymResponse;
import net.mkubik.gmms.api.model.GymEntry;
import net.mkubik.gmms.api.model.ListGymsResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClientResponseException;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GymE2ETest extends BaseE2ETest {

    @Test
    void createGymPersistsAndLists() {
        CreateGymResponse created = createGym(
                "Iron Temple",
                "Main Street 1, Warsaw",
                "+48111222333"
        );

        assertThat(created.getId()).isNotNull();
        assertThat(created.getName()).isEqualTo("Iron Temple");

        int dbCount = countRows("SELECT COUNT(*) FROM gym WHERE id = ?", created.getId());
        assertThat(dbCount).isEqualTo(1);

        ListGymsResponse listResponse = restClient.get()
                .uri("/gyms")
                .retrieve()
                .body(ListGymsResponse.class);

        assertThat(listResponse).isNotNull();
        assertThat(listResponse.getGyms())
                .extracting(GymEntry::getId)
                .contains(created.getId());
        assertThat(listResponse.getPage()).isPresent();
    }

    @Test
    void createGymWithDuplicateNameReturnsConflict() {
        CreateGymResponse created = createGym(
                "Metro Gym",
                "Main Street 99, Warsaw",
                "+48111444555"
        );

        UUID existingId = created.getId();
        assertThat(existingId).isNotNull();

        assertThatThrownBy(() -> createGym(
                "Metro Gym",
                "Other Street 11, Warsaw",
                "+48111999111"
        ))
                .isInstanceOf(RestClientResponseException.class)
                .satisfies(ex -> {
                    RestClientResponseException responseException = (RestClientResponseException) ex;
                    assertThat(responseException.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
                });
    }
}


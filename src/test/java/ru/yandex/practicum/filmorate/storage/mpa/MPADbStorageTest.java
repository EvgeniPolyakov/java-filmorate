package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.MPARating;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class MPADbStorageTest {
    private final MPAStorage mpaStorage;

    @Test
    @Transactional
    void getMPAByIdTest() {
        assertThat(mpaStorage.getMPAById(1L).getName()).isEqualTo("G");
        assertThat(mpaStorage.getMPAById(2L).getName()).isEqualTo("PG");
        assertThat(mpaStorage.getMPAById(3L).getName()).isEqualTo("PG-13");
        assertThat(mpaStorage.getMPAById(4L).getName()).isEqualTo("R");
        assertThat(mpaStorage.getMPAById(5L).getName()).isEqualTo("NC-17");
    }

    @Test
    @Transactional
    void getMPAValuesTest() {
        List<MPARating> allMPAs = new ArrayList<>();
        allMPAs.add(new MPARating(1L, "G"));
        allMPAs.add(new MPARating(2L, "PG"));
        allMPAs.add(new MPARating(3L, "PG-13"));
        allMPAs.add(new MPARating(4L, "R"));
        allMPAs.add(new MPARating(5L, "NC-17"));
        assertThat(mpaStorage.getMPAValues()).isEqualTo(allMPAs);
    }
}
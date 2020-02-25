package com.ibm.ftmpoc.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.ibm.ftmpoc.web.rest.TestUtil;

public class ShedularTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Shedular.class);
        Shedular shedular1 = new Shedular();
        shedular1.setId(1L);
        Shedular shedular2 = new Shedular();
        shedular2.setId(shedular1.getId());
        assertThat(shedular1).isEqualTo(shedular2);
        shedular2.setId(2L);
        assertThat(shedular1).isNotEqualTo(shedular2);
        shedular1.setId(null);
        assertThat(shedular1).isNotEqualTo(shedular2);
    }
}

package com.heekng.api_toche_web.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heekng.api_toche_web.dto.UnitDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@ActiveProfiles("real")
public class GuidApiControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void guidByUnitTest() throws Exception {
        String url = "/api/v1/guid/units?unitIds=162,163";

        mockMvc.perform(
                MockMvcRequestBuilders
                        .get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.units").isNotEmpty())
                .andExpect(jsonPath("$.resultCount").isNotEmpty())
                .andExpect(jsonPath("$.allUsedCount").isNotEmpty())
                .andDo(print());
    }

    @Test
    public void guidByAugmentTest() throws Exception {
        String url = "/api/v1/guid/augment?augmentIds=1027,1034&seasonId=14";

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get(url)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.units").isNotEmpty())
                .andExpect(jsonPath("$.resultCount").isNotEmpty())
                .andExpect(jsonPath("$.allUsedCount").isNotEmpty())
                .andDo(print());
    }
}
package com.heekng.api_toche_web.api;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@ActiveProfiles("real")
public class AugmentApiControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void augmentsTest() throws Exception {
        String url = "/api/v1/augments";

        mockMvc.perform(MockMvcRequestBuilders.get(url).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    assertThat(response.getContentAsString()).contains("id");
                })
                .andExpect(jsonPath("$.size()").exists())
                .andDo(print());
    }

    @Test
    public void augmentDetailTest() throws Exception {
        String url = "/api/v1/augments/1125";

        mockMvc.perform(MockMvcRequestBuilders.get(url).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("1125"))
                .andExpect(jsonPath("$.krName").value("이중 공중 보급"))
                .andDo(print());
    }

}
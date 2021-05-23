package com.qibill.lock.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @author qibill
 * @date 2021/5/17 0017 上午 11:00
 */
@SpringBootTest
public class GoodControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() { //MockMvcBuilders使用构建MockMvc对象   （项目拦截器有效）
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void buy_Goods() throws Exception {
        MultiValueMap<String, String> params1 = new LinkedMultiValueMap<>();

        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.get("/buy_goods").
                accept(MediaType.APPLICATION_JSON).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON).params(params1));
        perform.andReturn().getResponse().setCharacterEncoding("UTF-8");
        perform.
                andDo(MockMvcResultHandlers.print()).
                andExpect(MockMvcResultMatchers.status().isOk());
    }
}

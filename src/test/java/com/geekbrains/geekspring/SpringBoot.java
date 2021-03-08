package com.geekbrains.geekspring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SpringBoot {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void tryToStart() throws Exception {
        mockMvc.perform(get("/products/edit/0"))
                .andDo(print())
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void badCredentials() throws Exception {
        mockMvc.perform(formLogin("/login").user("moo").password("moo"))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }
}

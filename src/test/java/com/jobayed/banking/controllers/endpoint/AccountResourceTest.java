package com.jobayed.banking.controllers.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobayed.banking.controllers.dto.request.AccountRequest;
import com.jobayed.banking.controllers.dto.response.AccountResponse;
import com.jobayed.banking.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AccountResourceTest {

    private MockMvc mockMvc;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountResource accountResource;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(accountResource).build();
    }

    @Test
    void createAccount_Success() throws Exception {
        AccountRequest request = new AccountRequest();
        request.setFirstName("John");
        request.setLastName("Doe");

        AccountResponse response = new AccountResponse();
        response.setAccountNumber("12345");
        response.setFirstName("John");

        when(accountService.createAccount(any(AccountRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountNumber").value("12345"));
    }

    @Test
    void getAccount_Default_Success() throws Exception {
        AccountResponse response = new AccountResponse();
        response.setAccountNumber("12345");

        when(accountService.getAccount("12345", false)).thenReturn(response);

        mockMvc.perform(get("/api/accounts/12345"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value("12345"));
    }

    @Test
    void getAccount_WithLedger_Success() throws Exception {
        AccountResponse response = new AccountResponse();
        response.setAccountNumber("12345");

        when(accountService.getAccount("12345", true)).thenReturn(response);

        mockMvc.perform(get("/api/accounts/12345")
                .param("withLedger", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value("12345"));
    }
}

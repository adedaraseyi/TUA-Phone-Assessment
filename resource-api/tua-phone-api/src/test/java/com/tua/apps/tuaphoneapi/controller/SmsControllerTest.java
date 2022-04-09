package com.tua.apps.tuaphoneapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tua.apps.core.phone.entity.CoreAccount;
import com.tua.apps.core.phone.entity.CorePhoneNumber;
import com.tua.apps.tuaphoneapi.domain.AccountITFactory;
import com.tua.apps.tuaphoneapi.domain.PhoneITFactory;
import com.tua.apps.tuaphoneapi.dto.requests.InboundSMSRequest;
import com.tua.apps.tuaphoneapi.dto.requests.OutboundSMSRequest;
import com.tua.apps.tuaphoneapi.security.pojo.ImplicitAccountAuthenticationToken;
import com.tua.apps.tuaphoneapi.util.RandomUtil;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class SmsControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private AccountITFactory accountITFactory;
    @Autowired
    private PhoneITFactory phoneITFactory;

    private static Integer outBoundSMSCount = 0;

    @Test
    void inboundSms_success() throws Exception {
        CoreAccount account = accountITFactory.getAnyExistingAccount();
        CorePhoneNumber phoneNumber = phoneITFactory.getAnyPhoneNumberByAccountId(account.getId());

        InboundSMSRequest request = new InboundSMSRequest();
        request.setFrom(RandomUtil.randomNumericString(10));
        request.setTo(phoneNumber.getNumber());
        request.setText("Hello World");

        setupAuthorization(account);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/inbound/sms")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(request));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.message", Matchers.is("inbound sms ok")));
    }

    @Test
    void inboundSms_fromIsInvalid() throws Exception {
        CoreAccount account = accountITFactory.getAnyExistingAccount();
        CorePhoneNumber phoneNumber = phoneITFactory.getAnyPhoneNumberByAccountId(account.getId());

        InboundSMSRequest request = new InboundSMSRequest();
        request.setFrom(RandomUtil.randomNumericString(2));
        request.setTo(phoneNumber.getNumber());
        request.setText("Hello World");

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/inbound/sms")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(request));

        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.error", Matchers.containsString("from is invalid")));
    }

    @Test
    void inboundSms_toIsMissing() throws Exception {
        CoreAccount account = accountITFactory.getAnyExistingAccount();

        InboundSMSRequest request = new InboundSMSRequest();
        request.setFrom(RandomUtil.randomNumericString(2));
        request.setText("Hello World");

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/inbound/sms")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(request));

        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.error", Matchers.containsString("to is missing")));
    }

    @Test
    void outboundSms_success() throws Exception {
        CoreAccount account = accountITFactory.getAnyExistingAccount();
        CorePhoneNumber phoneNumber = phoneITFactory.getAnyPhoneNumberByAccountId(account.getId());

        OutboundSMSRequest request = new OutboundSMSRequest();
        request.setTo(RandomUtil.randomNumericString(14));
        request.setFrom(phoneNumber.getNumber());
        request.setText("Testing Outbound SMS");

        setupAuthorization(account);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/outbound/sms")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(request));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.message", Matchers.is("outbound sms ok")));
        outBoundSMSCount++;
    }

    @Test
    void outboundSms_stoppedPhoneNoPair() throws Exception {
        CoreAccount account = accountITFactory.getAnyExistingAccount();
        CorePhoneNumber phoneNumber1 = phoneITFactory.getAnyPhoneNumberByAccountId(account.getId());
        CorePhoneNumber phoneNumber2 = phoneITFactory.getAnyPhoneNumberByAccountId(account.getId());

        InboundSMSRequest request = new InboundSMSRequest();
        request.setFrom(phoneNumber1.getNumber());
        request.setTo(phoneNumber2.getNumber());
        request.setText("STOP");

        OutboundSMSRequest outboundSMSRequest = new OutboundSMSRequest();
        outboundSMSRequest.setFrom(phoneNumber1.getNumber());
        outboundSMSRequest.setTo(phoneNumber2.getNumber());
        outboundSMSRequest.setText("Testing Outbound SMS");

        setupAuthorization(account);

        MockHttpServletRequestBuilder inboundMockRequest = MockMvcRequestBuilders.post("/inbound/sms")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(request));

        mockMvc.perform(inboundMockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.message", Matchers.is("inbound sms ok")));

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/outbound/sms")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(outboundSMSRequest));

        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.error", Matchers.is(String.format("sms from %s to %s blocked by STOP request", outboundSMSRequest.getFrom(), outboundSMSRequest.getTo()))));
    }

    @Test
    void outboundSms_exceed50RequestsPerDay() throws Exception {
        CoreAccount account = accountITFactory.getAnyExistingAccount();
        CorePhoneNumber phoneNumber1 = phoneITFactory.getAnyPhoneNumberByAccountId(account.getId());

        OutboundSMSRequest outboundSMSRequest = new OutboundSMSRequest();
        outboundSMSRequest.setFrom(phoneNumber1.getNumber());
        outboundSMSRequest.setTo(RandomUtil.randomNumericString(8));
        outboundSMSRequest.setText("Testing Outbound SMS");

        setupAuthorization(account);

        while (outBoundSMSCount <= 50) {
            outboundSMSRequest.setTo(RandomUtil.randomNumericString(RandomUtil.randomInt(6, 16)));

            MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/outbound/sms")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(this.mapper.writeValueAsString(outboundSMSRequest));

            mockMvc.perform(mockRequest)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", Matchers.notNullValue()))
                    .andExpect(jsonPath("$.message", Matchers.is("outbound sms ok")));

            outBoundSMSCount++;
        }

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/outbound/sms")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(outboundSMSRequest));

        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$.error", Matchers.is(String.format("limit reached for from %s", outboundSMSRequest.getFrom()))));
    }

    private void setupAuthorization(CoreAccount account) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(new ImplicitAccountAuthenticationToken(account));
        SecurityContextHolder.setContext(context);
    }
}

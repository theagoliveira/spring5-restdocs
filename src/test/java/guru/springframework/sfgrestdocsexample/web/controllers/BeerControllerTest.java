package guru.springframework.sfgrestdocsexample.web.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import guru.springframework.sfgrestdocsexample.domain.Beer;
import guru.springframework.sfgrestdocsexample.repositories.BeerRepository;
import guru.springframework.sfgrestdocsexample.web.model.BeerDTO;
import guru.springframework.sfgrestdocsexample.web.model.BeerStyleEnum;

@WebMvcTest(BeerController.class)
@ComponentScan(basePackages = "guru.springframework.sfgrestdocsexample.web.mappers")
class BeerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    BeerRepository beerRepository;

    private static final Beer EMPTY_BEER = Beer.builder().build();
    private static final BeerDTO BEER_DTO = BeerDTO.builder()
                                                   .beerName("Nice Ale")
                                                   .beerStyle(BeerStyleEnum.ALE)
                                                   .price(new BigDecimal("9.99"))
                                                   .upc(123123123123L)
                                                   .build();

    @Test
    void testCreate() throws Exception {
        var beerDTOJson = objectMapper.writeValueAsString(BEER_DTO);

        mockMvc.perform(
            post(BeerController.BASE_URI).contentType(MediaType.APPLICATION_JSON)
                                         .content(beerDTOJson)
        ).andExpect(status().isCreated());
    }

    @Test
    void testShow() throws Exception {
        given(beerRepository.findById(any(UUID.class))).willReturn(Optional.of(EMPTY_BEER));

        mockMvc.perform(
            get(BeerController.BASE_URI + "/" + UUID.randomUUID().toString()).accept(
                MediaType.APPLICATION_JSON
            )
        ).andExpect(status().isOk());
    }

    @Test
    void testUpdate() throws Exception {
        var beerDTOJson = objectMapper.writeValueAsString(BEER_DTO);
        given(beerRepository.findById(any(UUID.class))).willReturn(Optional.of(EMPTY_BEER));

        mockMvc.perform(
            put(BeerController.BASE_URI + "/" + UUID.randomUUID().toString()).contentType(
                MediaType.APPLICATION_JSON
            ).content(beerDTOJson)
        ).andExpect(status().isNoContent());
    }

}

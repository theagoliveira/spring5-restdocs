package guru.springframework.sfgrestdocsexample.web.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.StringUtils;

import guru.springframework.sfgrestdocsexample.domain.Beer;
import guru.springframework.sfgrestdocsexample.repositories.BeerRepository;
import guru.springframework.sfgrestdocsexample.web.model.BeerDTO;
import guru.springframework.sfgrestdocsexample.web.model.BeerStyleEnum;

@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "dev.springframework.guru", uriPort = 80)
@WebMvcTest(BeerController.class)
@ComponentScan(basePackages = "guru.springframework.sfgrestdocsexample.web.mappers")
class BeerControllerTest {

    private static final String V1_BEER = "v1/beer";

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

        ConstrainedFields fields = new ConstrainedFields(BeerDTO.class);

        mockMvc.perform(
            post(BeerController.BASE_URI).contentType(MediaType.APPLICATION_JSON)
                                         .content(beerDTOJson)
        )
               .andExpect(status().isCreated())
               .andDo(
                   document(
                       V1_BEER + "-create",
                       requestFields(
                           fields.withPath("id").ignored(), fieldWithPath("version").ignored(),
                           fields.withPath("createdDate").ignored(),
                           fields.withPath("lastModifiedDate").ignored(),
                           fields.withPath("beerName").description("Beer name."),
                           fields.withPath("beerStyle").description("Beer style."),
                           fields.withPath("upc").description("Beer UPC.").attributes(),
                           fields.withPath("price").description("Beer price."),
                           fields.withPath("quantityOnHand").ignored()
                       )
                   )
               );
    }

    @Test
    void testShow() throws Exception {
        given(beerRepository.findById(any(UUID.class))).willReturn(Optional.of(EMPTY_BEER));

        mockMvc.perform(
            get(BeerController.BASE_URI + "/{id}", UUID.randomUUID().toString()).param(
                "isCold", "yes"
            ).accept(MediaType.APPLICATION_JSON)
        )
               .andExpect(status().isOk())
               .andDo(
                   document(
                       V1_BEER + "-show",
                       pathParameters(
                           parameterWithName("id").description("UUID of desired beer to get.")
                       ),
                       requestParameters(
                           parameterWithName("isCold").description("Is beer cold query param.")
                       ),
                       responseFields(
                           fieldWithPath("id").description("Beer id."),
                           fieldWithPath("version").description("Version number."),
                           fieldWithPath("createdDate").description("Creation date."),
                           fieldWithPath("lastModifiedDate").description("Update date."),
                           fieldWithPath("beerName").description("Beer name."),
                           fieldWithPath("beerStyle").description("Beer style."),
                           fieldWithPath("upc").description("Beer UPC."),
                           fieldWithPath("price").description("Beer price."),
                           fieldWithPath("quantityOnHand").description("Quantity on hand.")
                       )
                   )
               );
    }

    @Test
    void testUpdate() throws Exception {
        var beerDTOJson = objectMapper.writeValueAsString(BEER_DTO);
        given(beerRepository.findById(any(UUID.class))).willReturn(Optional.of(EMPTY_BEER));

        mockMvc.perform(
            put(BeerController.BASE_URI + "/{id}", UUID.randomUUID().toString()).contentType(
                MediaType.APPLICATION_JSON
            ).content(beerDTOJson)
        )
               .andExpect(status().isNoContent())
               .andDo(
                   document(
                       V1_BEER + "-update",
                       pathParameters(
                           parameterWithName("id").description("UUID of desired beer to update.")
                       )
                   )
               );
    }

    private static class ConstrainedFields {

        private final ConstraintDescriptions constraintDescriptions;

        ConstrainedFields(Class<?> input) {
            this.constraintDescriptions = new ConstraintDescriptions(input);
        }

        private FieldDescriptor withPath(String path) {
            return fieldWithPath(path).attributes(
                key("constraints").value(
                    StringUtils.collectionToDelimitedString(
                        this.constraintDescriptions.descriptionsForProperty(path), ". "
                    )
                )
            );
        }

    }

}

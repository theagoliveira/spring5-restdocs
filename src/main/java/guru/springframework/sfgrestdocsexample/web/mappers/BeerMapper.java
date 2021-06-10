package guru.springframework.sfgrestdocsexample.web.mappers;

import org.mapstruct.Mapper;

import guru.springframework.sfgrestdocsexample.domain.Beer;
import guru.springframework.sfgrestdocsexample.web.model.BeerDTO;

@Mapper(uses = {DateMapper.class})
public interface BeerMapper {

    BeerDTO BeerToBeerDTO(Beer beer);

    Beer BeerDTOToBeer(BeerDTO beerDTO);

}

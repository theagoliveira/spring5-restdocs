package guru.springframework.sfgrestdocsexample.web.controllers;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import guru.springframework.sfgrestdocsexample.repositories.BeerRepository;
import guru.springframework.sfgrestdocsexample.web.mappers.BeerMapper;
import guru.springframework.sfgrestdocsexample.web.model.BeerDTO;

@RestController
@RequestMapping(value = BeerController.BASE_URI)
public class BeerController {

    public static final String BASE_URI = "/api/v1/beers";

    private final BeerMapper beerMapper;
    private final BeerRepository beerRepository;

    public BeerController(BeerMapper beerMapper, BeerRepository beerRepository) {
        this.beerMapper = beerMapper;
        this.beerRepository = beerRepository;
    }

    @GetMapping
    public List<BeerDTO> index() {
        return beerRepository.findAll()
                             .stream()
                             .map(beerMapper::BeerToBeerDTO)
                             .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public BeerDTO show(@PathVariable UUID id) {
        return beerMapper.BeerToBeerDTO(beerRepository.findById(id).orElse(null));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void create(@RequestBody @Validated BeerDTO beerDTO) {
        beerRepository.save(beerMapper.BeerDTOToBeer(beerDTO));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void update(@PathVariable UUID id, @RequestBody @Validated BeerDTO beerDTO) {
        var optBeer = beerRepository.findById(id);

        if (optBeer.isPresent()) {
            var beer = optBeer.get();

            var beerName = beerDTO.getBeerName();
            var beerStyle = beerDTO.getBeerStyle().name();
            var price = beerDTO.getPrice();
            var upc = beerDTO.getUpc();

            if (beerName != null)
                beer.setBeerName(beerName);

            if (beerStyle != null)
                beer.setBeerStyle(beerStyle);

            if (price != null)
                beer.setPrice(price);

            if (upc != null)
                beer.setUpc(upc);

            beerRepository.save(beer);
        }
    }

}

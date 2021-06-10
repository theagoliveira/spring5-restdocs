package guru.springframework.sfgrestdocsexample.bootstrap;

import java.math.BigDecimal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import guru.springframework.sfgrestdocsexample.domain.Beer;
import guru.springframework.sfgrestdocsexample.repositories.BeerRepository;

@Component
public class Bootstrap implements CommandLineRunner {

    private final BeerRepository beerRepository;

    public Bootstrap(BeerRepository beerRepository) {
        this.beerRepository = beerRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (beerRepository.count() == 0)
            loadBeers();
    }

    private void loadBeers() {
        beerRepository.save(
            Beer.builder()
                .beerName("Mango Bobs")
                .beerStyle("IPA")
                .quantityToBrew(200)
                .minOnHand(12)
                .upc(337010000001L)
                .price(new BigDecimal("12.95"))
                .build()
        );

        beerRepository.save(
            Beer.builder()
                .beerName("Galaxy Cat")
                .beerStyle("PALE_ALE")
                .quantityToBrew(200)
                .minOnHand(12)
                .upc(337010000002L)
                .price(new BigDecimal("11.95"))
                .build()
        );
    }

}

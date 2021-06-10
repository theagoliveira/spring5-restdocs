package guru.springframework.sfgrestdocsexample.web.model;

import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class BeerPagedList extends PageImpl<BeerDTO> {

    public BeerPagedList(List<BeerDTO> content) {
        super(content);
    }

    public BeerPagedList(List<BeerDTO> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

}

package guru.springframework.sfgrestdocsexample.web.mappers;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import org.springframework.stereotype.Component;

@Component
public class DateMapper {

    public OffsetDateTime aOffsetDateTime(Timestamp ts) {
        if (ts != null) {
            return OffsetDateTime.of(
                ts.toLocalDateTime().getYear(), ts.toLocalDateTime().getMonthValue(),
                ts.toLocalDateTime().getDayOfMonth(), ts.toLocalDateTime().getHour(),
                ts.toLocalDateTime().getMinute(), ts.toLocalDateTime().getSecond(),
                ts.toLocalDateTime().getNano(), ZoneOffset.UTC
            );
        } else {
            return null;
        }
    }

    public Timestamp asTimestamp(OffsetDateTime odt) {
        if (odt != null) {
            return Timestamp.valueOf(odt.atZoneSameInstant(ZoneOffset.UTC).toLocalDateTime());
        } else {
            return null;
        }
    }

}

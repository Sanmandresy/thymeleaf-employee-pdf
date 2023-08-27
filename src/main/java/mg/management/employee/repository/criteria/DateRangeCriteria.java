package mg.management.employee.repository.criteria;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DateRangeCriteria {
  private Instant startDate;
  private Instant endDate;
}

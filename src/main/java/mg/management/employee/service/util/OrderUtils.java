package mg.management.employee.service.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class OrderUtils {
  static Integer PAGE = 1;
  static Integer PAGE_SIZE = 10;

  private OrderUtils() {

  }

  public static Pageable paginate(String order, Integer page, Integer pageSize) {
    String defaultOrder = "asc";
    String defaultSortProperty = "lastName";
    int defaultPage = 0;
    int defaultPageSize = 10;

    String finalOrder = order != null ? order : defaultOrder;
    int finalPage = page != null ? page : defaultPage;
    int finalPageSize = pageSize != null ? pageSize : defaultPageSize;

    if (finalOrder.equalsIgnoreCase("asc")) {
      return PageRequest.of(finalPage, finalPageSize, Sort.by(Sort.Direction.ASC, defaultSortProperty));
    } else if (finalOrder.equalsIgnoreCase("desc")) {
      return PageRequest.of(finalPage, finalPageSize, Sort.by(Sort.Direction.DESC, defaultSortProperty));
    }

    return PageRequest.of(finalPage, finalPageSize, Sort.by(Sort.Direction.ASC, defaultSortProperty));
  }


}

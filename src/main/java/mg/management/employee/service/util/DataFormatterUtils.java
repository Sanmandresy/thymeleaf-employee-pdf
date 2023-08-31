package mg.management.employee.service.util;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Base64;
import java.util.Date;
import java.util.Locale;
import mg.management.employee.model.AgeCriteria;
import org.springframework.web.multipart.MultipartFile;

import static mg.management.employee.model.AgeCriteria.BIRTHDAY;
import static mg.management.employee.model.AgeCriteria.YEAR_ONLY;

public class DataFormatterUtils {
  private DataFormatterUtils() {

  }

  public static int getAge(String birthdate, AgeCriteria criteria) {
    SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
    try {
      Date date = formatter.parse(birthdate);
      LocalDate birthLocalDate = LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault());
      LocalDate currentLocalDate = LocalDate.now();
      Period period = Period.between(birthLocalDate, currentLocalDate);
      if (criteria == BIRTHDAY) {
        return period.getYears();
      } else if (criteria == YEAR_ONLY) {
        return currentLocalDate.getYear() - birthLocalDate.getYear();
      }
      return period.getYears();

    } catch (ParseException e) {
      e.printStackTrace();
    }
    return 0;
  }

  public static Instant stringToInstant(String str) {
    if (str == null || str.isEmpty()) {
      return null;
    }
    LocalDate localDate = LocalDate.parse(str);
    return localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
  }


  public static String instantToCommonDate(Instant instant) {
    DateTimeFormatter formatter = DateTimeFormatter
        .ofLocalizedDate(FormatStyle.LONG)
        .withLocale(Locale.FRENCH)
        // Madagascar's timezone
        .withZone(ZoneId.of("UTC+3"));
    return formatter.format(instant);
  }

  public static byte[] multipartFileToByte(MultipartFile file) {
    if (file != null) {
      try {
        return file.getBytes();
      } catch (IOException e) {
        throw new RuntimeException("Error on mapping image");
      }
    }
    return null;
  }

  public static String byteAToBase64(byte[] file) {
    if (file != null) {
      return Base64.getEncoder().encodeToString(file);
    }
    return null;
  }

}

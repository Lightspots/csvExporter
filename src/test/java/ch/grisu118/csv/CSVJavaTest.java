package ch.grisu118.csv;

import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class CSVJavaTest {

  @Test
  public void testWithPOJO() {
    CSV csv = CSV.generate(Arrays.asList(
        new POJO("NullAtStart", false, null),
        new POJO("TestName", true, 42),
        new POJO("NullAtEnd", false, null)
    ));
    String expected = new CSVBuilder(Separator.COMMA)
        .newLine("Name", "Active", "number")
        .newLine("NullAtStart", "false", "")
        .newLine("TestName", "true", "42")
        .newLine("NullAtEnd", "false", "")
        .build();
    assertThat(csv.toString(), equalTo(expected));
  }

  static class POJO {

    @CSVField(header = "Name")
    private final String name;

    @CSVField(header = "Active")
    private final boolean active;

    @CSVField
    private final Integer number;

    POJO(String name, boolean active, Integer number) {
      this.name = name;
      this.active = active;
      this.number = number;
    }

  }
}

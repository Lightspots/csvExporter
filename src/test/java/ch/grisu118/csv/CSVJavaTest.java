package ch.grisu118.csv;

import org.junit.Test;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class CSVJavaTest {

  @Test
  public void testWithPOJO() {
    CSV csv = CSV.generate(Collections.singleton(new POJO("TestName", false, 42)));
    String expected = new CSVBuilder(Separator.COMMA)
        .newLine("Name", "Active", "number")
        .newLine("TestName", "false", "42")
        .build();
    assertThat(csv.toString(), equalTo(expected));
  }

  static class POJO {

    @CSVField(header = "Name")
    private final String name;

    @CSVField(header = "Active")
    private final boolean active;

    @CSVField
    private final int number;

    POJO(String name, boolean active, int number) {
      this.name = name;
      this.active = active;
      this.number = number;
    }

  }
}

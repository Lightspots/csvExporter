package ch.grisu118.csv;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class CSVJavaTest {

  @Test
  public void testWithPOJO() {
    CSV csv = CSV.generate(Collections.singleton(new POJO("TestName", false)));
    List<String> lines = Arrays.stream(csv.toString().split(System.lineSeparator())).collect(Collectors.toList());
    assertThat(lines, equalTo(Arrays.asList("\"Name\",\"Active\",", "\"TestName\",\"false\",")));
  }

  static class POJO {

    @CSVField(header = "Name")
    private final String name;

    @CSVField(header = "Active")
    private final boolean active;

    POJO(String name, boolean active) {
      this.name = name;
      this.active = active;
    }

  }
}

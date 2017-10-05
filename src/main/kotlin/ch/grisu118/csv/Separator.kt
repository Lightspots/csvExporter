package ch.grisu118.csv

enum class Separator(val value: String) {
  TAB("\t"),
  COMMA(","),
  SEMICOLON(";"),
  COLON(":"),
  SPACE(" ")
}
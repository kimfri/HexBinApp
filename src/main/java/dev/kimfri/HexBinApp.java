// src/main/java/dev/kimfri/HexBinApp.java
package dev.kimfri;

import java.util.Scanner;

public class HexBinApp {

  public static void main(String[] args) {
    new HexBinApp().run();
  }

  void run() {
    Scanner scanner = new Scanner(System.in);

    System.out.println(buildWelcomeMessage());

    while (true) {
      System.out.print("> ");
      String input = scanner.nextLine().trim();

      if (input.equalsIgnoreCase("q")) {
        System.out.println("Avslutar...");
        break;
      }

      if (input.isEmpty()) {
        continue;
      }

      try {
        byte[] bytes = parseHex(input);
        System.out.println(buildPrintOutput(bytes));
      } catch (IllegalArgumentException e) {
        System.out.println(buildErrorMessage(e.getMessage()));
      }
    }

    scanner.close();
  }

  String buildWelcomeMessage() {
    return new StringBuilder()
        .append("=== Byte Array Printer ===\n")
        .append("Ange bytes i något av följande format:\n")
        .append("  aa bb cc       (hex med mellanslag)\n")
        .append("  0xaa 0xbb 0xcc (hex med 0x-prefix)\n")
        .append("  aabbcc         (sammanslagen hex)\n")
        .append("Skriv 'q' för att avsluta.")
        .toString();
  }

  String buildErrorMessage(String message) {
    return new StringBuilder()
        .append("Ogiltigt värde: ").append(message).append("\n")
        .append("Accepterade format: 'aa bb cc', '0xaa 0xbb 0xcc', 'aabbcc'")
        .toString();
  }

  byte[] parseHex(String input) {
    String normalized = input.trim();

    if (normalized.contains("0x") || normalized.contains("0X")) {
      return parsePrefixedHex(normalized);
    }
    if (normalized.contains(" ")) {
      return parseSpaceSeparatedHex(normalized);
    }
    return parseConcatenatedHex(normalized);
  }

  byte[] parsePrefixedHex(String input) {
    String[] tokens = input.split("\\s+");
    byte[] bytes = new byte[tokens.length];
    for (int i = 0; i < tokens.length; i++) {
      String token = tokens[i];
      if (!token.matches("0[xX][0-9A-Fa-f]{1,2}")) {
        throw new IllegalArgumentException("'" + token + "' är inte ett giltigt 0x-värde");
      }
      bytes[i] = (byte) Integer.parseInt(token.substring(2), 16);
    }
    return bytes;
  }

  byte[] parseSpaceSeparatedHex(String input) {
    String[] tokens = input.split("\\s+");
    byte[] bytes = new byte[tokens.length];
    for (int i = 0; i < tokens.length; i++) {
      if (!tokens[i].matches("[0-9A-Fa-f]{1,2}")) {
        throw new IllegalArgumentException("'" + tokens[i] + "' är inte ett giltigt hex-värde");
      }
      bytes[i] = (byte) Integer.parseInt(tokens[i], 16);
    }
    return bytes;
  }

  byte[] parseConcatenatedHex(String input) {
    if (!input.matches("[0-9A-Fa-f]+")) {
      throw new IllegalArgumentException("'" + input + "' innehåller ogiltiga tecken");
    }
    if (input.length() % 2 != 0) {
      throw new IllegalArgumentException("Sammanslagen hex måste ha jämnt antal tecken");
    }
    byte[] bytes = new byte[input.length() / 2];
    for (int i = 0; i < bytes.length; i++) {
      bytes[i] = (byte) Integer.parseInt(input.substring(i * 2, i * 2 + 2), 16);
    }
    return bytes;
  }

  String buildPrintOutput(byte[] bytes) {
    return new StringBuilder()
        .append("=== Hex ===\n")
        .append("Original:  ").append(toHex(bytes)).append("\n")
        .append("Inverted:  ").append(toHex(invert(bytes))).append("\n")
        .append("Reversed:  ").append(toHex(reverse(bytes))).append("\n")
        .append("\n=== Binary ===\n")
        .append("Original:  ").append(toBinary(bytes)).append("\n")
        .append("Inverted:  ").append(toBinary(invert(bytes))).append("\n")
        .append("Reversed:  ").append(toBinary(reverse(bytes)))
        .toString();
  }

  String toHex(byte[] bytes) {
    StringBuilder sb = new StringBuilder();
    for (byte b : bytes) {
      sb.append(String.format("%02X ", b));
    }
    return sb.toString().trim();
  }

  String toBinary(byte[] bytes) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < bytes.length; i++) {
      String bits = String.format("%8s", Integer.toBinaryString(bytes[i] & 0xFF)).replace(' ', '0');
      sb.append(bits);
      if (i < bytes.length - 1) {
        sb.append(" ");
      }
    }
    return sb.toString();
  }

  byte[] invert(byte[] bytes) {
    byte[] result = new byte[bytes.length];
    for (int i = 0; i < bytes.length; i++) {
      result[i] = (byte) ~bytes[i];
    }
    return result;
  }

  byte[] reverse(byte[] bytes) {
    byte[] result = new byte[bytes.length];
    for (int i = 0; i < bytes.length; i++) {
      result[i] = bytes[bytes.length - 1 - i];
    }
    return result;
  }
}

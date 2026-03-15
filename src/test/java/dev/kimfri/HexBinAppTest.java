package dev.kimfri;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class HexBinAppTest {

  private HexBinApp app;

  @BeforeEach
  void setUp() {
    app = new HexBinApp();
  }

  static Stream<Arguments> buildPrintOutputCases() {
    return Stream.of(
        Arguments.of("contains hex section header",    new byte[]{0x1F},       "=== Hex ==="),
        Arguments.of("contains binary section header", new byte[]{0x1F},       "=== Binary ==="),
        Arguments.of("contains original hex",          new byte[]{0x1F, 0x4A}, "1F 4A"),
        Arguments.of("contains inverted hex",          new byte[]{0x1F},       "E0"),
        Arguments.of("contains reversed hex",          new byte[]{0x1F, 0x4A}, "4A 1F"),
        Arguments.of("contains original binary",       new byte[]{0x1F},       "00011111"),
        Arguments.of("contains inverted binary",       new byte[]{0x1F},       "11100000"),
        Arguments.of("contains reversed binary",       new byte[]{0x1F, 0x4A}, "01001010 00011111")
    );
  }

  @Nested
  class BuildWelcomeMessage {

    @Test
    void given_noInput_when_buildWelcomeMessage_then_containsTitle() {
      String result = app.buildWelcomeMessage();
      assertTrue(result.contains("=== Byte Array Printer ==="));
    }

    @Test
    void given_noInput_when_buildWelcomeMessage_then_containsAllFormats() {
      String result = app.buildWelcomeMessage();
      assertTrue(result.contains("aa bb cc"));
      assertTrue(result.contains("0xaa 0xbb 0xcc"));
      assertTrue(result.contains("aabbcc"));
    }

    @Test
    void given_noInput_when_buildWelcomeMessage_then_containsQuitInstruction() {
      String result = app.buildWelcomeMessage();
      assertTrue(result.contains("'q'"));
    }
  }

  @Nested
  class BuildErrorMessage {

    @Test
    void given_errorMessage_when_buildErrorMessage_then_containsMessage() {
      String result = app.buildErrorMessage("något gick fel");
      assertTrue(result.contains("något gick fel"));
    }

    @Test
    void given_errorMessage_when_buildErrorMessage_then_containsAcceptedFormats() {
      String result = app.buildErrorMessage("något gick fel");
      assertTrue(result.contains("aa bb cc"));
      assertTrue(result.contains("0xaa 0xbb 0xcc"));
      assertTrue(result.contains("aabbcc"));
    }
  }

  @Nested
  class ParseHex {

    @Nested
    class SpaceSeparatedFormat {

      @Test
      void given_spaceSeparatedHex_when_parseHex_then_returnsCorrectBytes() {
        byte[] result = app.parseHex("1F 4A B3");
        assertArrayEquals(new byte[]{0x1F, 0x4A, (byte) 0xB3}, result);
      }

      @Test
      void given_singleByte_when_parseHex_then_returnsOneByte() {
        byte[] result = app.parseHex("FF");
        assertArrayEquals(new byte[]{(byte) 0xFF}, result);
      }

      @Test
      void given_invalidToken_when_parseHex_then_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> app.parseHex("1F XY"));
      }
    }

    @Nested
    class PrefixedFormat {

      @Test
      void given_prefixedHex_when_parseHex_then_returnsCorrectBytes() {
        byte[] result = app.parseHex("0x1F 0x4A 0xB3");
        assertArrayEquals(new byte[]{0x1F, 0x4A, (byte) 0xB3}, result);
      }

      @Test
      void given_uppercasePrefix_when_parseHex_then_returnsCorrectBytes() {
        byte[] result = app.parseHex("0X1F 0XFF");
        assertArrayEquals(new byte[]{0x1F, (byte) 0xFF}, result);
      }

      @Test
      void given_invalidPrefixedToken_when_parseHex_then_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> app.parseHex("0x1F 0xXY"));
      }
    }

    @Nested
    class ConcatenatedFormat {

      @Test
      void given_concatenatedHex_when_parseHex_then_returnsCorrectBytes() {
        byte[] result = app.parseHex("1F4AB3");
        assertArrayEquals(new byte[]{0x1F, 0x4A, (byte) 0xB3}, result);
      }

      @Test
      void given_oddLengthConcatenated_when_parseHex_then_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> app.parseHex("1F4"));
      }

      @Test
      void given_invalidCharacters_when_parseHex_then_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> app.parseHex("1FGG"));
      }
    }
  }

  @Nested
  class ParsePrefixedHex {

    @Test
    void given_prefixedHex_when_parsePrefixedHex_then_returnsCorrectBytes() {
      byte[] result = app.parsePrefixedHex("0x1F 0x4A 0xB3");
      assertArrayEquals(new byte[]{0x1F, 0x4A, (byte) 0xB3}, result);
    }

    @Test
    void given_uppercasePrefix_when_parsePrefixedHex_then_returnsCorrectBytes() {
      byte[] result = app.parsePrefixedHex("0X1F 0XFF");
      assertArrayEquals(new byte[]{0x1F, (byte) 0xFF}, result);
    }

    @Test
    void given_invalidToken_when_parsePrefixedHex_then_throwsIllegalArgumentException() {
      assertThrows(IllegalArgumentException.class, () -> app.parsePrefixedHex("0x1F 0xXY"));
    }
  }

  @Nested
  class ParseSpaceSeparatedHex {

    @Test
    void given_spaceSeparatedHex_when_parseSpaceSeparatedHex_then_returnsCorrectBytes() {
      byte[] result = app.parseSpaceSeparatedHex("1F 4A B3");
      assertArrayEquals(new byte[]{0x1F, 0x4A, (byte) 0xB3}, result);
    }

    @Test
    void given_singleByte_when_parseSpaceSeparatedHex_then_returnsOneByte() {
      byte[] result = app.parseSpaceSeparatedHex("FF");
      assertArrayEquals(new byte[]{(byte) 0xFF}, result);
    }

    @Test
    void given_invalidToken_when_parseSpaceSeparatedHex_then_throwsIllegalArgumentException() {
      assertThrows(IllegalArgumentException.class, () -> app.parseSpaceSeparatedHex("1F XY"));
    }
  }

  @Nested
  class ParseConcatenatedHex {

    @Test
    void given_concatenatedHex_when_parseConcatenatedHex_then_returnsCorrectBytes() {
      byte[] result = app.parseConcatenatedHex("1F4AB3");
      assertArrayEquals(new byte[]{0x1F, 0x4A, (byte) 0xB3}, result);
    }

    @Test
    void given_oddLengthInput_when_parseConcatenatedHex_then_throwsIllegalArgumentException() {
      assertThrows(IllegalArgumentException.class, () -> app.parseConcatenatedHex("1F4"));
    }

    @Test
    void given_invalidCharacters_when_parseConcatenatedHex_then_throwsIllegalArgumentException() {
      assertThrows(IllegalArgumentException.class, () -> app.parseConcatenatedHex("1FGG"));
    }
  }

  @Nested
  class BuildPrintOutput {

    @ParameterizedTest(name = "{0}")
    @MethodSource("dev.kimfri.HexBinAppTest#buildPrintOutputCases")
    void given_byteArray_when_buildPrintOutput_then_containsExpectedString(
        String description, byte[] input, String expected) {
      String result = app.buildPrintOutput(input);
      assertTrue(result.contains(expected));
    }
  }

  @Nested
  class ToHex {

    @Test
    void given_singleByte_when_toHex_then_returnsUppercaseHex() {
      String result = app.toHex(new byte[]{0x1F});
      assertEquals("1F", result);
    }

    @Test
    void given_multipleBytes_when_toHex_then_returnsSpaceSeparated() {
      String result = app.toHex(new byte[]{0x1F, 0x4A, (byte) 0xB3});
      assertEquals("1F 4A B3", result);
    }

    @Test
    void given_zeroByte_when_toHex_then_returnsPaddedZero() {
      String result = app.toHex(new byte[]{0x00});
      assertEquals("00", result);
    }
  }

  @Nested
  class ToBinary {

    @Test
    void given_singleByte_when_toBinary_then_returnsEightBits() {
      String result = app.toBinary(new byte[]{0x1F});
      assertEquals("00011111", result);
    }

    @Test
    void given_multipleBytes_when_toBinary_then_returnsSpaceSeparated() {
      String result = app.toBinary(new byte[]{0x1F, (byte) 0xFF});
      assertEquals("00011111 11111111", result);
    }

    @Test
    void given_zeroByte_when_toBinary_then_returnsAllZeros() {
      String result = app.toBinary(new byte[]{0x00});
      assertEquals("00000000", result);
    }
  }

  @Nested
  class Invert {

    @Test
    void given_zeroByte_when_invert_then_returnsAllOnes() {
      byte[] result = app.invert(new byte[]{0x00});
      assertArrayEquals(new byte[]{(byte) 0xFF}, result);
    }

    @Test
    void given_allOnesByte_when_invert_then_returnsZero() {
      byte[] result = app.invert(new byte[]{(byte) 0xFF});
      assertArrayEquals(new byte[]{0x00}, result);
    }

    @Test
    void given_multipleBytes_when_invert_then_invertsEachByte() {
      byte[] result = app.invert(new byte[]{0x1F, (byte) 0xB3});
      assertArrayEquals(new byte[]{(byte) 0xE0, 0x4C}, result);
    }

    @Test
    void given_byteArray_when_invert_then_doesNotMutateInput() {
      byte[] input = {0x1F, 0x4A};
      app.invert(input);
      assertArrayEquals(new byte[]{0x1F, 0x4A}, input);
    }
  }

  @Nested
  class Reverse {

    @Test
    void given_multipleBytes_when_reverse_then_returnsReversedOrder() {
      byte[] result = app.reverse(new byte[]{0x1F, 0x4A, (byte) 0xB3});
      assertArrayEquals(new byte[]{(byte) 0xB3, 0x4A, 0x1F}, result);
    }

    @Test
    void given_singleByte_when_reverse_then_returnsSameByte() {
      byte[] result = app.reverse(new byte[]{0x1F});
      assertArrayEquals(new byte[]{0x1F}, result);
    }

    @Test
    void given_byteArray_when_reverse_then_doesNotMutateInput() {
      byte[] input = {0x1F, 0x4A};
      app.reverse(input);
      assertArrayEquals(new byte[]{0x1F, 0x4A}, input);
    }
  }
}

# HexBinApp

Interaktiv kommandoradsapplikation som läser byte-arrayer från stdin och skriver ut dem i hex- och binärformat. Varje inmatning visas som original, inverterad och reverserad representation.

## Funktioner

- Tre inputformat stöds: mellanslags-separerad hex, 0x-prefixad hex och sammanslagen hex
- Skriver ut original-, inverterad- och reverserad representation i både hex och binär
- Interaktiv loop – mata in nya värden tills du väljer att avsluta

## Krav

- Java 17 eller senare
- Maven 3.8 eller senare

## Bygga och köra

```bash
# Bygg projektet
mvn package

# Kör applikationen
java -jar target/hexbinapp-1.0.jar
```

## Användning

Starta applikationen och mata in bytes i något av följande format:

```
=== Byte Array Printer ===
Ange bytes i något av följande format:
  aa bb cc       (hex med mellanslag)
  0xaa 0xbb 0xcc (hex med 0x-prefix)
  aabbcc         (sammanslagen hex)
Skriv 'q' för att avsluta.

> 1F 4A B3
=== Hex ===
Original:  1F 4A B3
Inverted:  E0 B5 4C
Reversed:  B3 4A 1F

=== Binary ===
Original:  00011111 01001010 10110011
Inverted:  11100000 10110101 01001100
Reversed:  10110011 01001010 00011111

> 0xFF 0x00
=== Hex ===
Original:  FF 00
Inverted:  00 FF
Reversed:  00 FF
...

> q
Avslutar...
```

### Inputformat

| Format            | Exempel           | Beskrivning                        |
|-------------------|-------------------|------------------------------------|
| Mellanslags-sep.  | `aa bb cc`        | Hex-värden separerade med mellanslag |
| 0x-prefixad       | `0xaa 0xbb 0xcc`  | Hex-värden med `0x`- eller `0X`-prefix |
| Sammanslagen      | `aabbcc`          | Hex-värden utan separator (jämnt antal tecken) |

## Projektstruktur

```
src/
├── main/java/dev/kimfri/
│   └── HexBinApp.java
└── test/java/dev/kimfri/
    └── HexBinAppTest.java
```

## Tester

Projektet använder JUnit 5 med `@Nested`-klasser och `given_when_then`-namngivning. Kör testerna med:

```bash
mvn test
```

## Beroenden

| Artefakt         | Version | Scope |
|------------------|---------|-------|
| `junit-jupiter`  | 5.10.2  | test  |

## Licens

MIT

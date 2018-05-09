
ASNEEDED = "-Wl,--as-needed"

ASNEEDED_pn-distcc = ""
ASNEEDED_pn-openobex = ""
ASNEEDED_pn-icu = ""
ASNEEDED_pn-pciutils = ""
ASNEEDED_pn-puzzles = ""
ASNEEDED_pn-rpm = ""

TARGET_LDFLAGS += "${ASNEEDED}"

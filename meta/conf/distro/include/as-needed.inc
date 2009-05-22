
ASNEEDED = "-Wl,--as-needed"

ASNEEDED_pn-console-tools = ""
ASNEEDED_pn-distcc = ""
ASNEEDED_pn-openobex = ""
ASNEEDED_pn-icu = ""
ASNEEDED_pn-xserver-kdrive-xomap = ""
ASNEEDED_pn-minimo = ""
ASNEEDED_pn-pciutils = ""
ASNEEDED_pn-puzzles = ""
ASNEEDED_pn-dialer = ""
ASNEEDED_pn-pulseaudio = ""

TARGET_LDFLAGS += "${ASNEEDED}"
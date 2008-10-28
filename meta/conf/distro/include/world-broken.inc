#
# Exclude known broken packages
#
# Needs old OpenSync
EXCLUDE_FROM_WORLD_pn-libsync = "1"
EXCLUDE_FROM_WORLD_pn-libopensync-plugin-evo2-sync = "1"
EXCLUDE_FROM_WORLD_pn-libopensync-plugin-file-sync = "1"
EXCLUDE_FROM_WORLD_pn-libopensync-plugin-irmc = "1"
EXCLUDE_FROM_WORLD_pn-libopensync-plugin-irmc-sync = "1"
EXCLUDE_FROM_WORLD_pn-libopensync-plugin-google-calendar = "1"
# Needs python
EXCLUDE_FROM_WORLD_pn-opkg-utils = "1"
# Needs gcc 3.x
EXCLUDE_FROM_WORLD_pn-qemu = "1"
# temporary
EXCLUDE_FROM_WORLD_pn-lirc = "1"
EXCLUDE_FROM_WORLD_pn-lirc-modules = "1"
# these applications need clutter 0.6
EXCLUDE_FROM_WORLD_pn-aaina = "1"
EXCLUDE_FROM_WORLD_pn-table = "1"
EXCLUDE_FROM_WORLD_pn-task-poky-clutter = "1"
# Need bzr
EXCLUDE_FROM_WORLD_pn-eds-tools = "1"
# Need fixed Perl - OE bug #3403
EXCLUDE_FROM_WORLD_pn-libxml-parser-perl = "1"
# Needs libxml-parser-perl
EXCLUDE_FROM_WORLD_pn-libxml-simple-perl = "1"
# Needs gtk+ <= 2.10
EXCLUDE_FROM_WORLD_pn-gcalctool = "1"

# The packages from these clash atm
EXCLUDE_FROM_WORLD_pn-abiword = "1"
EXCLUDE_FROM_WORLD_pn-abiword-embedded = "1"

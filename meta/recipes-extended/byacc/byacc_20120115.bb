PR = "r0"

# Sigh. This is one of those places where everyone licenses it differently. Someone
# even apply UCB to it (Free/Net/OpenBSD). The maintainer states that:
# "I've found no reliable source which states that byacc must bear a UCB copyright."
# Setting to PD as this is what the upstream has it as.

LICENSE="PD"
LIC_FILES_CHKSUM = "file://package/debian/copyright;md5=f186cf0d59bac042b75830396ec389a3"
require byacc.inc

SRC_URI[md5sum] = "3061c62c47ec0f43255afd8fc3f7e3ab"
SRC_URI[sha256sum] = "4034efc9b59646fc61e29b1a87472cccf1d1aaf45435f9d3bf58dec2b5f0831c"

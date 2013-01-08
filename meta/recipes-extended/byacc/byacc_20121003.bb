PR = "r0"

# Sigh. This is one of those places where everyone licenses it differently. Someone
# even apply UCB to it (Free/Net/OpenBSD). The maintainer states that:
# "I've found no reliable source which states that byacc must bear a UCB copyright."
# Setting to PD as this is what the upstream has it as.

LICENSE="PD"
LIC_FILES_CHKSUM = "file://package/debian/copyright;md5=f186cf0d59bac042b75830396ec389a3"
require byacc.inc

SRC_URI[md5sum] = "26ba97e33a3bfaa311e2e5e1e078de55"
SRC_URI[sha256sum] = "5afa4863550a7fc528e976cc33cc383bc0a9637ab95a6a594d2d5f6bf08dd1c5"


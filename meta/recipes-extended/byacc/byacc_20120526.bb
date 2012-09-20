PR = "r1"

# Sigh. This is one of those places where everyone licenses it differently. Someone
# even apply UCB to it (Free/Net/OpenBSD). The maintainer states that:
# "I've found no reliable source which states that byacc must bear a UCB copyright."
# Setting to PD as this is what the upstream has it as.

LICENSE="PD"
LIC_FILES_CHKSUM = "file://package/debian/copyright;md5=f186cf0d59bac042b75830396ec389a3"
require byacc.inc

SRC_URI[md5sum] = "d8a9959f784205092762eb29426bdf23"
SRC_URI[sha256sum] = "be05856b039bc9eb95a93cf89ac381de4fc6bb91d236c6b130e8b1daeb6075d6"

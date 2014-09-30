# Sigh. This is one of those places where everyone licenses it differently. Someone
# even apply UCB to it (Free/Net/OpenBSD). The maintainer states that:
# "I've found no reliable source which states that byacc must bear a UCB copyright."
# Setting to PD as this is what the upstream has it as.

LICENSE = "PD"
LIC_FILES_CHKSUM = "file://package/debian/copyright;md5=f186cf0d59bac042b75830396ec389a3"
require byacc.inc

SRC_URI[md5sum] = "814d2ebedd738a526e5aeb00b1ad38af"
SRC_URI[sha256sum] = "ecd0d87bf7e62b5536fb096ada96ecd564c6411866bbe6644e30ee7963e97fe5"

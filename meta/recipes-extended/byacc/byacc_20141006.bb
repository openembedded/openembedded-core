# Sigh. This is one of those places where everyone licenses it differently. Someone
# even apply UCB to it (Free/Net/OpenBSD). The maintainer states that:
# "I've found no reliable source which states that byacc must bear a UCB copyright."
# Setting to PD as this is what the upstream has it as.

LICENSE = "PD"
LIC_FILES_CHKSUM = "file://package/debian/copyright;md5=f186cf0d59bac042b75830396ec389a3"
require byacc.inc

SRC_URI[md5sum] = "62d58192f2995f24cef41a7cb8ec9ba3"
SRC_URI[sha256sum] = "391b0ac550e94920a86960c6973ec539784dc84849e7c2bb1645ae6d8178b14d"

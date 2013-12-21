# Sigh. This is one of those places where everyone licenses it differently. Someone
# even apply UCB to it (Free/Net/OpenBSD). The maintainer states that:
# "I've found no reliable source which states that byacc must bear a UCB copyright."
# Setting to PD as this is what the upstream has it as.

LICENSE = "PD"
LIC_FILES_CHKSUM = "file://package/debian/copyright;md5=f186cf0d59bac042b75830396ec389a3"
require byacc.inc

SRC_URI[md5sum] = "265f74058b923d7d9ddd1bd82f05a42e"
SRC_URI[sha256sum] = "e2560caab28396fc732a946e722cf5c6220e72388e70cdcc23bf67ae88799b31"

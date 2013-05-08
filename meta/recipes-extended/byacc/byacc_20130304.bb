# Sigh. This is one of those places where everyone licenses it differently. Someone
# even apply UCB to it (Free/Net/OpenBSD). The maintainer states that:
# "I've found no reliable source which states that byacc must bear a UCB copyright."
# Setting to PD as this is what the upstream has it as.

LICENSE="PD"
LIC_FILES_CHKSUM = "file://package/debian/copyright;md5=f186cf0d59bac042b75830396ec389a3"
require byacc.inc

SRC_URI[md5sum] = "e5eef96a33f55b724f2a957f0fa3b019"
SRC_URI[sha256sum] = "dd8397174e088baf57cf4d344023a34b0758b7bbb8a1dfcdafdc234b8eb57924"

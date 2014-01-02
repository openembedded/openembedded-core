# Sigh. This is one of those places where everyone licenses it differently. Someone
# even apply UCB to it (Free/Net/OpenBSD). The maintainer states that:
# "I've found no reliable source which states that byacc must bear a UCB copyright."
# Setting to PD as this is what the upstream has it as.

LICENSE = "PD"
LIC_FILES_CHKSUM = "file://package/debian/copyright;md5=f186cf0d59bac042b75830396ec389a3"
require byacc.inc

SRC_URI[md5sum] = "05aeba2369b90fa63bac5e7425090f9d"
SRC_URI[sha256sum] = "78aea5a34ccb6c9f1ff673a94cd3e66b0f669b7a042c2fcfdada85f0f399421c"

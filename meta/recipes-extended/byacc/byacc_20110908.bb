PR = "r1"

# Sigh. This is one of those places where everyone licenses it differently. Someone
# even apply UCB to it (Free/Net/OpenBSD). The maintainer states that:
# "I've found no reliable source which states that byacc must bear a UCB copyright."
# Setting to PD as this is what the upstream has it as.

LICENSE="PD"
LIC_FILES_CHKSUM = "file://package/debian/copyright;md5=4dc4c30f840a7203fb6edf20b3db849e"
require byacc.inc

SRC_URI[md5sum] = "5665ee1f2ca482d57eef9591a2fe4768"
SRC_URI[sha256sum] = "70a3e491ddfae9da04646abe769fa0ecb9cbf003e2a09c5732907e3e8b478fb0"

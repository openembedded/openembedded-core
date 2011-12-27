PR = "r0"

# Sigh. This is one of those places where everyone licenses it differently. Someone
# even apply UCB to it (Free/Net/OpenBSD). The maintainer states that:
# "I've found no reliable source which states that byacc must bear a UCB copyright."
# Setting to PD as this is what the upstream has it as.

LICENSE="PD"
LIC_FILES_CHKSUM = "file://package/debian/copyright;md5=4dc4c30f840a7203fb6edf20b3db849e"
require byacc.inc

SRC_URI[md5sum] = "c17542fd9df6e392d495a64f883e29f1"
SRC_URI[sha256sum] = "ca37eb3702a02efe4a4dd09e0ef26fc91e4c22c36d8d52e45d2f76923fbd99d9"

# Sigh. This is one of those places where everyone licenses it differently. Someone
# even apply UCB to it (Free/Net/OpenBSD). The maintainer states that:
# "I've found no reliable source which states that byacc must bear a UCB copyright."
# Setting to PD as this is what the upstream has it as.

LICENSE = "PD"
LIC_FILES_CHKSUM = "file://package/debian/copyright;md5=74533d32ffd38bca4cbf1f1305f8bc60"
require byacc.inc

SRC_URI[md5sum] = "bde0463c6c03f059b1e6e9c5579cbe49"
SRC_URI[sha256sum] = "178e08f7ab59edfb16d64902b7a9d78592d2d8d3ee30ab7a967188d969589b5a"


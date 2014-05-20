# Sigh. This is one of those places where everyone licenses it differently. Someone
# even apply UCB to it (Free/Net/OpenBSD). The maintainer states that:
# "I've found no reliable source which states that byacc must bear a UCB copyright."
# Setting to PD as this is what the upstream has it as.

LICENSE = "PD"
LIC_FILES_CHKSUM = "file://package/debian/copyright;md5=f186cf0d59bac042b75830396ec389a3"
require byacc.inc

SRC_URI[md5sum] = "e7c13c5e207dc05eab9145cc9972397b"
SRC_URI[sha256sum] = "2f104c7e200dd86844d5f3521e12cb55fc48a9c3da3480a65fde2ca8c053bdcc"

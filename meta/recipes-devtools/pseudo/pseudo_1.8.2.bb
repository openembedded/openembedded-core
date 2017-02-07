require pseudo.inc

SRC_URI = "http://downloads.yoctoproject.org/releases/pseudo/${BPN}-${PV}.tar.bz2 \
           file://0001-configure-Prune-PIE-flags.patch \
           file://fallback-passwd \
           file://fallback-group \
           file://moreretries.patch \
           "

SRC_URI[md5sum] = "7d41e72188fbea1f696c399c1a435675"
SRC_URI[sha256sum] = "ceb456bd47770a37ca20784a91d715c5a7601e07e26ab11b0c77e9203ed3d196"

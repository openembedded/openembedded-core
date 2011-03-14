SUMMARY = "Helpers for AV applications using UPnP"
DESCRIPTION = "GUPnP-AV is a collection of helpers for building AV (audio/video) applications using GUPnP."
LICENSE = "LGPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=3bf50002aefd002f49e7bb854063f7e7 \
                    file://libgupnp-av/gupnp-av.h;beginline=1;endline=22;md5=d344132a8766641fcb11213ff5982086"
DEPENDS = "gupnp"

SRC_URI = "http://gupnp.org/sites/all/files/sources/${BPN}-${PV}.tar.gz"

PR = "r0"

inherit autotools pkgconfig

SRC_URI[md5sum] = "6eace748e9869cddb27daab46eb50635"
SRC_URI[sha256sum] = "e13018d24abec9fafd75ef59c365adbb2ea3712fe5e69fff8c7284dc3f13cdeb"

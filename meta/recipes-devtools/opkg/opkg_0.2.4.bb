require opkg.inc

SRC_URI = "http://downloads.yoctoproject.org/releases/${BPN}/${BPN}-${PV}.tar.gz \
           file://no-install-recommends.patch \
           file://add-exclude.patch \
           file://opkg-configure.service \
           file://libopkg-opkg_remove.c-avoid-remove-pkg-repeatly-with.patch \
"

S = "${WORKDIR}/${BPN}-${PV}"

SRC_URI[md5sum] = "40ed2aee15abc8d550539449630091bd"
SRC_URI[sha256sum] = "0f40c7e457d81edf9aedc07c778f4697111ab163a38ef95999faece015453086"

require opkg.inc

SRC_URI = "http://downloads.yoctoproject.org/releases/${BPN}/${BPN}-${PV}.tar.gz \
           file://no-install-recommends.patch \
           file://add-exclude.patch \
           file://opkg-configure.service \
           file://libopkg-opkg_remove.c-avoid-remove-pkg-repeatly-with.patch \
"

S = "${WORKDIR}/${BPN}-${PV}"

SRC_URI[md5sum] = "b3ecef90d67d2aed2a14c2116a027482"
SRC_URI[sha256sum] = "aa554ce7538544aac4f69e8274a0f9b8b433b8c3b1d00704bd393f713303a12b"

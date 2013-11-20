require opkg.inc

SRC_URI = "https://opkg.googlecode.com/files/${BPN}-${PV}.tar.gz \
           file://no-install-recommends.patch \
           file://add-exclude.patch \
           file://opkg-configure.service \
"

S = "${WORKDIR}/${BPN}-${PV}"

SRC_URI[md5sum] = "e8a6fd34fb2529191fe09dc14c934cc3"
SRC_URI[sha256sum] = "81b7055eb4c12c5e5652339305c9236cf357890717d4bea063963f3f434d966f"

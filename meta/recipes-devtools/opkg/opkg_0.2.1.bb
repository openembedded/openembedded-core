require opkg.inc

SRC_URI = "http://downloads.yoctoproject.org/releases/${BPN}/${BPN}-${PV}.tar.gz \
           file://no-install-recommends.patch \
           file://add-exclude.patch \
           file://opkg-configure.service \
"

S = "${WORKDIR}/${BPN}-${PV}"

SRC_URI[md5sum] = "1881d170b9dfbd7ecf0aa468cb9779c0"
SRC_URI[sha256sum] = "43c2d95e4cd3ef5e341e233e63de78698ec7522bca446972963160bb0f1e62db"

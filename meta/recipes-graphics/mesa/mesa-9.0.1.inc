SRC_URI = "ftp://ftp.freedesktop.org/pub/mesa/${PV}/MesaLib-${PV}.tar.bz2 \
           file://0002-cross-compile.patch \
           file://cross-glsl.patch \
           "

SRC_URI[md5sum] = "97d6554c05ea7449398afe3a0ede7018"
SRC_URI[sha256sum] = "57a7ba83bace3f3a7dc0929fe026ef5f0ad454234ec4ad8a3e0d31650a75db6f"

LIC_FILES_CHKSUM = "file://docs/license.html;md5=012f69938d81b766472fd0d240df38a4"

S = "${WORKDIR}/Mesa-${PV}"

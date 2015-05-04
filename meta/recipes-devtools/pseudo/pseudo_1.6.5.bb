require pseudo.inc

SRC_URI = " \
    http://www.yoctoproject.org/downloads/${BPN}/${BPN}-${PV}.tar.bz2 \
    file://fallback-passwd \
    file://fallback-group \
"

SRC_URI[md5sum] = "a5545ff365e243193c81981df98e870b"
SRC_URI[sha256sum] = "f5a0af976c958915503f516dd02097ab6a64c98a3c0cb4ceb0ce8b1ef8e38b3c"

PSEUDO_EXTRA_OPTS ?= "--enable-force-async --without-passwd-fallback"

do_install_append_class-native () {
	install -d ${D}${sysconfdir}
	# The fallback files should never be modified
	install -m 444 ${WORKDIR}/fallback-passwd ${D}${sysconfdir}/passwd
	install -m 444 ${WORKDIR}/fallback-group ${D}${sysconfdir}/group
}

require pseudo.inc

SRC_URI = " \
    http://www.yoctoproject.org/downloads/${BPN}/${BPN}-${PV}.tar.bz2 \
    file://fallback-passwd \
    file://fallback-group \
"

SRC_URI[md5sum] = "2fb800c90d643bfce55e1ce5ca67f3b3"
SRC_URI[sha256sum] = "25a7528f9191f74cceccc08a90c00086f2b3a9f6b900ea419a4f092de9a06775"

PSEUDO_EXTRA_OPTS ?= "--enable-force-async --without-passwd-fallback"

do_install_append_class-native () {
	install -d ${D}${sysconfdir}
	# The fallback files should never be modified
	install -m 444 ${WORKDIR}/fallback-passwd ${D}${sysconfdir}/passwd
	install -m 444 ${WORKDIR}/fallback-group ${D}${sysconfdir}/group
}

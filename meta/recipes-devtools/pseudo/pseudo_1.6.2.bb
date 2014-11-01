require pseudo.inc

SRC_URI = " \
    http://www.yoctoproject.org/downloads/${BPN}/${BPN}-${PV}.tar.bz2 \
    file://0001-pseudo_client.c-protect-pwd_lck-against-magic.patch \
    file://0002-pseudo_util-modify-interface-to-pseudo_etc_file.patch \
    file://0003-pseudo_client.c-support-multiple-directories-in-PSEU.patch \
    file://fallback-passwd \
    file://fallback-group \
"

SRC_URI[md5sum] = "4d7b4f9d1b4aafa680ce94a5a9a52f1f"
SRC_URI[sha256sum] = "c72be92689511ced7c419149c6aaa1b1a9e4dfc6409d1f16ab72cc35bc1e376a"

PSEUDO_EXTRA_OPTS ?= "--enable-force-async --without-passwd-fallback"

do_install_append_class-native () {
	install -d ${D}${sysconfdir}
	# The fallback files should never be modified
	install -m 444 ${WORKDIR}/fallback-passwd ${D}${sysconfdir}/passwd
	install -m 444 ${WORKDIR}/fallback-group ${D}${sysconfdir}/group
}

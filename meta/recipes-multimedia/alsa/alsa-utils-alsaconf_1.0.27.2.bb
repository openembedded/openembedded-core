require alsa-utils_${PV}.bb

FILESEXTRAPATHS_prepend := "${THISDIR}/alsa-utils:"

PACKAGES = "${PN}"
RDEPENDS_${PN} += "bash"

DESCRIPTION_${PN}     = "a bash script that creates ALSA configuration files"
FILES_${PN} = "${sbindir}/alsaconf"

S = "${WORKDIR}/alsa-utils-${PV}"

do_install() {
	install -d ${D}${sbindir}
	install -m 0755 ${S}/alsaconf/alsaconf ${D}${sbindir}/
}

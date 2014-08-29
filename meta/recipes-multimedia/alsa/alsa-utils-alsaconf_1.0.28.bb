require alsa-utils_${PV}.bb

SUMMARY = "Shell script that creates ALSA configuration files"

FILESEXTRAPATHS_prepend := "${THISDIR}/alsa-utils:"

PACKAGES = "${PN}"
RDEPENDS_${PN} += "bash"

FILES_${PN} = "${sbindir}/alsaconf"

S = "${WORKDIR}/alsa-utils-${PV}"

do_install() {
	install -d ${D}${sbindir}
	install -m 0755 ${S}/alsaconf/alsaconf ${D}${sbindir}/
}

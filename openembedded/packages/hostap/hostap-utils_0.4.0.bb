DESCRIPTION = "User mode helpers for the hostap driver"
HOMEPAGE = "http://hostap.epitest.fi"
SECTION = "kernel/userland"
PRIORITY = "optional"
MAINTAINER = "Michael 'Mickey' Lauer <mickey@Vanille.de>"
LICENSE = "GPL"
DEPENDS = "virtual/kernel-hostap"
RRECOMMENDS = "virtual/kernel-hostap"
PR = "r3"

SRC_URI = "http://hostap.epitest.fi/releases/hostap-utils-${PV}.tar.gz \
	   file://hostap-fw-load.patch;patch=1"
S = "${WORKDIR}/hostap-utils-${PV}"

BINARIES = "hostap_crypt_conf hostap_diag hostap_fw_load hostap_io_debug \
	    hostap_rid prism2_param prism2_srec split_combined_hex"

do_install() {
	install -d ${D}${sbindir}/
	for f in ${BINARIES}
	do
		install -m 0755 $f ${D}${sbindir}/
	done
}


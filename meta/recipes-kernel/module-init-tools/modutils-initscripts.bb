SECTION = "base"
DESCRIPTION = "modutils configuration files"
LICENSE = "PD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=7bf87fc37976e93ec66ad84fac58c098"
SRC_URI = "file://modutils.sh \
	   file://PD.patch"
PR = "r6"

INITSCRIPT_NAME = "modutils.sh"
INITSCRIPT_PARAMS = "start 4 S ."

inherit update-rc.d

do_compile () {
}

do_install () {
	install -d ${D}${sysconfdir}/init.d/
	install -m 0755 ${WORKDIR}/modutils.sh ${D}${sysconfdir}/init.d/
}

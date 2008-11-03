DESCRIPTION = "Super readahead, part of the fastboot tool set."
SECTION = "base"
HOMEPAGE = "http://www.moblin.org"
LICENSE = "GPLv2"
PR = "r12"

inherit update-rc.d

SRC_URI = "http://www.moblin.org/sites/all/files/sreadahead-${PV}.tar.gz \
	  file://sreadahead-0.02-make.patch;patch=1 \
	  file://readahead_c.patch;patch=1 \
	  file://sreadahead-generate.sh \
	  file://sreadahead.sh"

CFLAGS_prepend = "-I ${S}/include "

PACKAGES += "${PN}-generate"
FILES_${PN} = "${base_sbindir}/sreadahead ${sysconfdir}/init.d/sreadahead.sh"
FILES_${PN}-generate = "${base_sbindir}/generate_filelist ${sysconfdir}/init.d/sreadahead-generate.sh"

INITSCRIPT_PACKAGES = "${PN} ${PN}-generate"
INITSCRIPT_NAME = "sreadahead.sh"
INITSCRIPT_NAME_${PN}-generate = "sreadahead-generate.sh" 
INITSCRIPT_PARAMS = "start 00 S ."
INITSCRIPT_PARAMS_${PN}-generate = "defaults 99"

do_install() {
	oe_runmake install DESTDIR=${D}
	install -d ${D}${sysconfdir}/init.d
	install -m 755 ${WORKDIR}/sreadahead.sh ${D}${sysconfdir}/init.d/
	install -m 755 ${WORKDIR}/sreadahead-generate.sh ${D}${sysconfdir}/init.d/
}

pkg_postinst_${PN}-generate () {
	# can't do this offline
	if [ "x$D" != "x" ]; then
		exit 1
	fi

	touch /etc/readahead.packed.first 
}

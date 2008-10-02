HOMEPAGE = "http://linux.duke.edu/projects/yum/"

SRC_URI = "http://linux.duke.edu/projects/yum/download/3.2/yum-${PV}.tar.gz \
           file://hacks.patch;patch=1 \
           file://paths.patch;patch=1 \
	   file://yum-install-recommends.py \
	   file://extract-postinst.awk"

DEPENDS = "rpm python python-iniparse python-urlgrabber yum-metadata-parser libxml2"

S = "${WORKDIR}/yum-${PV}"

inherit autotools

do_compile_append () {
	sed -e 's#!/usr/bin/python#!${bindir}/python#' -e 's#/usr/share#${datadir}#' -i ${S}/bin/yum.py
	sed -e 's#!/usr/bin/python#!${bindir}/python#' -e 's#/usr/share#${datadir}#' -i ${S}/bin/yum-updatesd.py
}

do_install_append () {
	install -d ${D}${bindir}/
	install ${WORKDIR}/extract-postinst.awk ${D}${bindir}/
	install ${WORKDIR}/yum-install-recommends.py ${D}${bindir}/
}

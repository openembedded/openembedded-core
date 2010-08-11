DESCRIPTION = "yum package manger is an automatic updater for rpm."
HOMEPAGE = "http://yum.baseurl.org/"
BUGTRACKER = "http://yum.baseurl.org/report"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=18810669f13b87348459e611d31ab760 \
                    file://yum/sqlutils.py;beginline=2;endline=14;md5=d704ae6a9d69ce90768ab9188236b992"

RDEPENDS = "python-rpm python-core python-iniparse python-urlgrabber \
            python-shell python-re python-logging python-pickle \
	    python-netserver python-compression \
	    python-unixadmin python-xml python-sqlite3 \
	    python-textutils python-fcntl python-email \
	    yum-metadata-parser"

PR = "r9"

SRC_URI = "http://yum.baseurl.org/download/3.2/yum-${PV}.tar.gz \
           file://paths.patch;apply=yes \
           file://paths2.patch;apply=yes \
	   file://yum-install-recommends.py \
	   file://extract-postinst.awk \
	   file://98_yum"

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
	rmdir ${D}${localstatedir}/cache/yum
	rmdir ${D}${localstatedir}/cache
	install -d ${D}/etc/default/volatiles
	install -m 0644 ${WORKDIR}/98_yum ${D}/etc/default/volatiles
}

FILES_${PN} += "${libdir}/python* ${datadir}/yum-cli"

BBCLASSEXTEND = "native"

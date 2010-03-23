HOMEPAGE = "http://linux.duke.edu/projects/yum/"
PR = "r7"

SRC_URI = "http://linux.duke.edu/projects/yum/download/3.2/yum-${PV}.tar.gz \
           file://paths.patch;patch=1 \
           file://paths2.patch;patch=1 \
	   file://yum-install-recommends.py \
	   file://extract-postinst.awk \
	   file://98_yum"

RDEPENDS = "python-rpm python-core python-iniparse python-urlgrabber \
            python-shell python-re python-logging python-pickle \
	    python-netserver python-compression \
	    python-unixadmin python-xml python-sqlite3 \
	    python-textutils python-fcntl python-email \
	    yum-metadata-parser"

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

pkg_postinst_yum () {
	/etc/init.d/populate-volatile.sh update
}

FILES_${PN} += "${libdir}/python* ${datadir}/yum-cli"

BBCLASSEXTEND = "native"
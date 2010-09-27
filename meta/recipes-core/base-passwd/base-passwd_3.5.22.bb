SUMMARY = "Base system master password/group files."
DESCRIPTION = "The master copies of the user database files (/etc/passwd and /etc/group).  The update-passwd tool is also provided to keep the system databases synchronized with these master files."
SECTION = "base"
PR = "r0"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=eb723b61539feef013de476e68b5c50a"

SRC_URI = "${DEBIAN_MIRROR}/main/b/base-passwd/base-passwd_${PV}.tar.gz \
           file://nobash.patch \
           file://root-home.patch"

S = "${WORKDIR}/base-passwd"

inherit autotools

do_install () {
	install -d -m 755 ${D}${sbindir}
	install -p -m 755 update-passwd ${D}${sbindir}/
	install -d -m 755 ${D}${mandir}/man8 ${D}${mandir}/pl/man8
	install -p -m 644 man/update-passwd.8 ${D}${mandir}/man8/
	install -p -m 644 man/update-passwd.pl.8 \
		${D}${mandir}/pl/man8/update-passwd.8
	gzip -9 ${D}${mandir}/man8/* ${D}${mandir}/pl/man8/*
	install -d -m 755 ${D}${datadir}/base-passwd
	install -p -m 644 passwd.master ${D}${datadir}/base-passwd/
	install -p -m 644 group.master ${D}${datadir}/base-passwd/

	install -d -m 755 ${D}${docdir}/${PN}
	install -p -m 644 debian/changelog ${D}${docdir}/${PN}/
	gzip -9 ${D}${docdir}/${PN}/*
	install -p -m 644 README ${D}${docdir}/${PN}/
	install -p -m 644 debian/copyright ${D}${docdir}/${PN}/
}

do_install_append_openmn() {
	echo "0:Jn6tcg/qjqvUE:0:0:root:/root:/bin/sh" >>${D}${datadir}/base-passwd/passwd.master
}


pkg_postinst () {
	set -e

	if [ ! -e $D${sysconfdir}/passwd ] ; then
		cp $D${datadir}/base-passwd/passwd.master $D${sysconfdir}/passwd
	fi

	if [ ! -e $D${sysconfdir}/group ] ; then
		cp $D${datadir}/base-passwd/group.master $D${sysconfdir}/group
	fi
	exit 0
}

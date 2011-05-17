SUMMARY = "Liberation(tm) Fonts"
DESCRIPTION = "The Liberation(tm) Fonts is a font family originally \
created by Ascender(c) which aims at metric compatibility with \
Arial, Times New Roman, Courier New."
HOMEPAGE = "https://fedorahosted.org/liberation-fonts/"
BUGTRACKER = "https://bugzilla.redhat.com/"

SECTION = "x11/fonts"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe"
RDEPENDS_${PN} = "fontconfig-utils"
PE = "1"
PR = "r2"

FONTREV = "0.20100721"
SRC_URI = "https://fedorahosted.org/releases/l/i/${BPN}/${BPN}-${PV}.${FONTREV}.tar.gz \
           file://30-liberation-aliases.conf"

S = ${WORKDIR}/${BPN}-${PV}.${FONTREV}

inherit allarch

do_install () {
	install -d ${D}${datadir}/fonts/ttf/
	for i in *.ttf; do
		install -m 0644 $i ${D}${prefix}/share/fonts/ttf/${i}
	done

	install -d ${D}${sysconfdir}/fonts/conf.d/
	install -m 0644 ${WORKDIR}/30-liberation-aliases.conf ${D}${sysconfdir}/fonts/conf.d/

	install -d ${D}${prefix}/share/doc/${PN}/
	install -m 0644 License.txt ${D}${datadir}/doc/${PN}/
}

pkg_postinst_${PN} () {
#!/bin/sh
if [ "x$D" != "x" ] ; then
	exit 1
fi
fc-cache
}

PACKAGES = "${PN}"
FILES_${PN} += "${sysconfdir} ${datadir}"

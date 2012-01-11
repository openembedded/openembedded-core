DESCRIPTION = "hdparm is a Linux shell utility for viewing \
and manipulating various IDE drive and driver parameters."
SECTION = "console/utils"
PRIORITY = "optional"
LICENSE = "BSD"
LICENSE_wiper = "GPLv2"
LIC_FILES_CHKSUM = "file://LICENSE.TXT;md5=910a8a42c962d238619c75fdb78bdb24 \
                    file://debian/copyright;md5=a82d7ba3ade9e8ec902749db98c592f3 \
                    file://wiper/GPLv2.txt;md5=fcb02dc552a041dee27e4b85c7396067 \
                    file://wiper/wiper.sh;beginline=7;endline=31;md5=b7bc642addc152ea307505bf1a296f09"

PACKAGES += "wiper"

FILES_wiper = "${bindir}/wiper.sh"
FILES_${PN} = "${base_sbindir} ${mandir}"

RDEPENDS_wiper = "bash gawk stat"

SRC_URI = "${SOURCEFORGE_MIRROR}/hdparm/hdparm-${PV}.tar.gz "
SRC_URI[md5sum] = "0bb94ddd1bedd5c02b1ca62f1caaf6de"
SRC_URI[sha256sum] = "87ede0a7cb5b4de500748232e9d251b75acfccce078e1d42c8d125b9e5b4ccd3"

do_install () {
	install -d ${D}/${base_sbindir} ${D}/${mandir}/man8 ${D}/${bindir}
	oe_runmake 'DESTDIR=${D}' 'sbindir=${base_sbindir}' install
	mv ${D}${base_sbindir}/hdparm ${D}${base_sbindir}/hdparm.${PN}
	cp ${S}/wiper/wiper.sh ${D}/${bindir}
}

pkg_postinst_${PN} () {
	update-alternatives --install ${base_sbindir}/hdparm hdparm hdparm.${PN} 100
}

pkg_prerm_${PN} () {
	update-alternatives --remove hdparm hdparm.${PN}
}

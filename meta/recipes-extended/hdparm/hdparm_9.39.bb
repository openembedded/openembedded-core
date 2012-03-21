DESCRIPTION = "hdparm is a Linux shell utility for viewing \
and manipulating various IDE drive and driver parameters."
SECTION = "console/utils"
LICENSE = "BSD"
LICENSE_wiper = "GPLv2"
LIC_FILES_CHKSUM = "file://LICENSE.TXT;md5=910a8a42c962d238619c75fdb78bdb24 \
                    file://debian/copyright;md5=a82d7ba3ade9e8ec902749db98c592f3 \
                    file://wiper/GPLv2.txt;md5=fcb02dc552a041dee27e4b85c7396067 \
                    file://wiper/wiper.sh;beginline=7;endline=31;md5=b7bc642addc152ea307505bf1a296f09"

PR = "r1"

PACKAGES += "wiper"

FILES_wiper = "${bindir}/wiper.sh"
FILES_${PN} = "${base_sbindir} ${mandir}"

RDEPENDS_wiper = "bash gawk stat"

SRC_URI = "${SOURCEFORGE_MIRROR}/hdparm/hdparm-${PV}.tar.gz "

SRC_URI[md5sum] = "2bc17b72403885d4faf959682944243b"
SRC_URI[sha256sum] = "72d550af4526aa96f0841c79321a0ee39d636cbaf1f294e52193e90c054b3cea"

EXTRA_OEMAKE += 'STRIP="echo"'

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

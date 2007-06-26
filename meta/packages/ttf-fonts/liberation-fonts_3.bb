DESCRIPTION = "The  fonts - TTF Edition"
SECTION = "x11/fonts"
PRIORITY = "optional"
LICENSE = "GPLv2"
PACKAGE_ARCH = "all"
RDEPENDS = "fontconfig-utils"

SRC_URI = "https://www.redhat.com/f/fonts/liberation-fonts-ttf-3.tar.gz \
           file://30-liberation-aliases.conf"

S = "${WORKDIR}/${PN}-0.2/"

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

pkg_postinst () {
#!/bin/sh
fc-cache
}

PACKAGES = "${PN}"
FILES_${PN} += "${sysconfdir} ${datadir}"

HOMEPAGE = "http://android.git.kernel.org/?p=platform/frameworks/base.git;a=tree;f=data/fonts"

DESCRIPTION = "The Droid typeface family was designed in the fall of 2006 by Ascender's \
               Steve Matteson, as a commission from Google to create a set of system fonts \
               for its Android platform. The goal was to provide optimal quality and comfort \
               on a mobile handset when rendered in application menus, web browsers and for \
               other screen text."

SRC_URI = "http://pokylinux.org/sources/droid-fonts-1.0.112.tar.gz \
           file://droid-fonts-sans-fontconfig.conf \
           file://droid-fonts-sans-mono-fontconfig.conf \
           file://droid-fonts-serif-fontconfig.conf "

LICENSE = "Apache License, Version 2.0"
SECTION = "x11/fonts"
RDEPENDS = "fontconfig-utils"
PACKAGES = "${PN}"
FILES_${PN} += "${sysconfdir} ${datadir}"

do_install () {
	install -d ${D}${datadir}/fonts/ttf/
	for i in *.ttf; do
		install -m 0644 $i ${D}${prefix}/share/fonts/ttf/${i}
	done

	install -d ${D}${sysconfdir}/fonts/conf.d/
	install -m 0644 ${WORKDIR}/*.conf ${D}${sysconfdir}/fonts/conf.d/

	install -d ${D}${prefix}/share/doc/${PN}/
	install -m 0644 README.txt ${D}${datadir}/doc/${PN}/
	install -m 0644 NOTICE ${D}${datadir}/doc/${PN}/
}

pkg_postinst () {
#!/bin/sh
fc-cache
}


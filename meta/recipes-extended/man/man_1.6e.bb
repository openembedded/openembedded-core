SUMMARY = "Online documentation tools"
DESCRIPTION = "A set of documentation tools: man, apropos and whatis"
SECTION = "console/utils"
HOMEPAGE = "http://primates.ximian.com/~flucifredi/man"
PRIORITY = "required"
LICENSE = "GPLv2"
PR = "r0"

DEPENDS = "groff less"

LIC_FILES_CHKSUM = "file://COPYING;md5=8ca43cbc842c2336e835926c2166c28b"

SRC_URI = "http://primates.ximian.com/~flucifredi/man/man-1.6e.tar.gz \
           file://man-1.5k-confpath.patch;striplevel=0 \
           file://man-1.5h1-make.patch;striplevel=1 \
           file://man-1.5k-nonascii.patch;striplevel=1 \
           file://man-1.6e-security.patch;striplevel=1 \
           file://man-1.6e-mandirs.patch;striplevel=1 \
           file://man-1.5m2-bug11621.patch;striplevel=1 \
           file://man-1.5k-sofix.patch;striplevel=1 \
           file://man-1.5m2-buildroot.patch;striplevel=1 \
           file://man-1.6e-ro_usr.patch;striplevel=1 \
           file://man-1.5i2-newline.patch;striplevel=0 \
           file://man-1.5j-utf8.patch;striplevel=1 \
           file://man-1.5i2-overflow.patch;striplevel=1 \
           file://man-1.5j-nocache.patch;striplevel=1 \
           file://man-1.5i2-initial.patch;striplevel=1 \
           file://man-1.5h1-gencat.patch;striplevel=0 \
           file://man-1.5g-nonrootbuild.patch;striplevel=1 \
           file://man-1.5m2-tv_fhs.patch;striplevel=0 \
           file://man-1.5j-i18n.patch;striplevel=1 \
           file://man-1.6e-whatis2.patch;striplevel=1 \
           file://man-1.6e-use_i18n_vars_in_a_std_way.patch;striplevel=1 \
           file://man-1.5m2-no-color-for-printing.patch;striplevel=1 \
           file://man-1.5m2-sigpipe.patch;striplevel=1 \
           file://man-1.6e-i18n_whatis.patch;striplevel=1 \
           file://man-1.6e-new_sections.patch;striplevel=1 \
           file://man-1.6e-lzma+xz-support.patch;striplevel=1 \
           file://man*"

SRC_URI[md5sum] = "d8187cd756398baefc48ba7d60ff6a8a"
SRC_URI[sha256sum] = "022faf23844eabb3662eabb105836925dd83bedda10271e2450a5bc5b61a5b5f"


do_configure () {
        ${S}/configure -default -confdir ${D}/etc +sgid +fhs +lang all
}


fakeroot do_install() {
        oe_runmake install DESTDIR=${D}
}

do_install_append(){
	mkdir -p  ${D}/etc/
        mkdir -p ${D}${datadir}/man/man5
        mkdir -p ${D}${datadir}/man/man7
	cp ${S}/src/man.conf ${D}/etc/
        cp ${WORKDIR}/man.1.gz ${D}${datadir}/man/man1/
        cp ${WORKDIR}/man.7.gz ${D}${datadir}/man/man7/
        cp ${WORKDIR}/manpath.5.gz ${D}${datadir}/man/man5/
}


FILES_${PN} += "${datadir}/locale"
